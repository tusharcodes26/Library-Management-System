package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

import service.AdminService;
import service.BookService;
import service.MemberService;
import service.IssueBookService;
import model.Book;
import model.Member;
import model.IssuedBook;
import util.JwtHelper;

public class ApiServer {
    private HttpServer server;
    private final AdminService adminService = new AdminService();
    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final IssueBookService issueService = new IssueBookService();

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/books", new BookHandler());
        server.createContext("/api/members", new MemberHandler());
        server.createContext("/api/issues", new IssueHandler());

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("REST API Server started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("REST API Server stopped.");
        }
    }

    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private boolean isAuthorized(HttpExchange exchange) {
        List<String> authHeaders = exchange.getRequestHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            return false;
        }
        String authHeader = authHeaders.get(0);
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return JwtHelper.verifyToken(token);
        }
        return false;
    }

    private boolean handleOptions(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            setCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        setCorsHeaders(exchange);
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            return bos.toString(StandardCharsets.UTF_8.name());
        }
    }

    private Map<String, String> parseQueryParams(URI uri) {
        Map<String, String> queryParams = new HashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length > 0) {
                    String key = keyValue[0];
                    String value = keyValue.length > 1 ? keyValue[1] : "";
                    try {
                        value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                    } catch (Exception e) {
                        // ignore
                    }
                    queryParams.put(key, value);
                }
            }
        }
        return queryParams;
    }

    private class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    String body = readRequestBody(exchange);
                    Map<String, String> req = ApiHelper.parseJson(body);
                    String username = req.get("username");
                    String password = req.get("password");

                    if (username == null || password == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing username or password\"}");
                        return;
                    }

                    boolean success = adminService.login(username, password);
                    if (success) {
                        String token = JwtHelper.generateToken(username);
                        sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Login successful\",\"token\":\"" + token + "\"}");
                    } else {
                        sendResponse(exchange, 401, "{\"status\":\"error\",\"message\":\"Invalid username or password\"}");
                    }
                } catch (Exception e) {
                    sendResponse(exchange, 500, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
                }
            } else {
                sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
            }
        }
    }

    private class BookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!isAuthorized(exchange)) {
                sendResponse(exchange, 401, "{\"status\":\"error\",\"message\":\"Unauthorized access: Missing or invalid token\"}");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if (path.endsWith("/search")) {
                    if ("GET".equalsIgnoreCase(method)) {
                        Map<String, String> query = parseQueryParams(exchange.getRequestURI());
                        String keyword = query.getOrDefault("keyword", "");
                        List<Book> books = bookService.searchBook(keyword);
                        sendResponse(exchange, 200, ApiHelper.booksToJson(books));
                    } else {
                        sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                    }
                    return;
                }

                if ("GET".equalsIgnoreCase(method)) {
                    List<Book> books = bookService.getAllBooks();
                    sendResponse(exchange, 200, ApiHelper.booksToJson(books));
                } else if ("POST".equalsIgnoreCase(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> req = ApiHelper.parseJson(body);
                    String title = req.get("title");
                    String author = req.get("author");
                    String category = req.get("category");
                    String quantityStr = req.get("quantity");

                    if (title == null || author == null || category == null || quantityStr == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required fields: title, author, category, quantity\"}");
                        return;
                    }

                    int quantity = Integer.parseInt(quantityStr);
                    Book book = new Book(0, title, author, category, quantity);
                    boolean success = bookService.addBook(book);
                    if (success) {
                        sendResponse(exchange, 201, "{\"status\":\"success\",\"message\":\"Book added successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to add book. Check validation rules.\"}");
                    }
                } else if ("PUT".equalsIgnoreCase(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> req = ApiHelper.parseJson(body);
                    String bookIdStr = req.get("bookId");
                    String title = req.get("title");
                    String author = req.get("author");
                    String category = req.get("category");
                    String quantityStr = req.get("quantity");

                    if (bookIdStr == null || title == null || author == null || category == null || quantityStr == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required fields: bookId, title, author, category, quantity\"}");
                        return;
                    }

                    int bookId = Integer.parseInt(bookIdStr);
                    int quantity = Integer.parseInt(quantityStr);
                    Book book = new Book(bookId, title, author, category, quantity);
                    boolean success = bookService.updateBook(book);
                    if (success) {
                        sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Book updated successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to update book\"}");
                    }
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI());
                    String idStr = query.get("id");
                    if (idStr == null) {
                        // try to read from body
                        String body = readRequestBody(exchange);
                        Map<String, String> req = ApiHelper.parseJson(body);
                        idStr = req.get("bookId");
                    }

                    if (idStr == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing bookId parameter or request body value\"}");
                        return;
                    }

                    int id = Integer.parseInt(idStr);
                    boolean success = bookService.deleteBook(id);
                    if (success) {
                        sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Book deleted successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to delete book or book ID not found\"}");
                    }
                } else {
                    sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                }
            } catch (util.ValidationException e) {
                sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Invalid numeric format in request body or parameter\"}");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class MemberHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!isAuthorized(exchange)) {
                sendResponse(exchange, 401, "{\"status\":\"error\",\"message\":\"Unauthorized access: Missing or invalid token\"}");
                return;
            }

            String method = exchange.getRequestMethod();
            try {
                if ("GET".equalsIgnoreCase(method)) {
                    List<Member> members = memberService.getAllMembers();
                    sendResponse(exchange, 200, ApiHelper.membersToJson(members));
                } else if ("POST".equalsIgnoreCase(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> req = ApiHelper.parseJson(body);
                    String name = req.get("name");
                    String email = req.get("email");
                    String phone = req.get("phone");

                    if (name == null || email == null || phone == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required fields: name, email, phone\"}");
                        return;
                    }

                    Member member = new Member(0, name, email, phone);
                    boolean success = memberService.addMember(member);
                    if (success) {
                        sendResponse(exchange, 201, "{\"status\":\"success\",\"message\":\"Member added successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to add member. Check validation rules.\"}");
                    }
                } else if ("PUT".equalsIgnoreCase(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> req = ApiHelper.parseJson(body);
                    String memberIdStr = req.get("memberId");
                    String name = req.get("name");
                    String email = req.get("email");
                    String phone = req.get("phone");

                    if (memberIdStr == null || name == null || email == null || phone == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required fields: memberId, name, email, phone\"}");
                        return;
                    }

                    int memberId = Integer.parseInt(memberIdStr);
                    Member member = new Member(memberId, name, email, phone);
                    boolean success = memberService.updateMember(member);
                    if (success) {
                        sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Member updated successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to update member\"}");
                    }
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI());
                    String idStr = query.get("id");
                    if (idStr == null) {
                        String body = readRequestBody(exchange);
                        Map<String, String> req = ApiHelper.parseJson(body);
                        idStr = req.get("memberId");
                    }

                    if (idStr == null) {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing memberId parameter or request body value\"}");
                        return;
                    }

                    int id = Integer.parseInt(idStr);
                    boolean success = memberService.deleteMember(id);
                    if (success) {
                        sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Member deleted successfully\"}");
                    } else {
                        sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to delete member or member ID not found\"}");
                    }
                } else {
                    sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                }
            } catch (util.ValidationException e) {
                sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Invalid numeric format in request body or parameter\"}");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
            }
        }
    }

    private class IssueHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!isAuthorized(exchange)) {
                sendResponse(exchange, 401, "{\"status\":\"error\",\"message\":\"Unauthorized access: Missing or invalid token\"}");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if (path.endsWith("/issue")) {
                    if ("POST".equalsIgnoreCase(method)) {
                        String body = readRequestBody(exchange);
                        Map<String, String> req = ApiHelper.parseJson(body);
                        String bookIdStr = req.get("bookId");
                        String memberIdStr = req.get("memberId");

                        if (bookIdStr == null || memberIdStr == null) {
                            sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required fields: bookId, memberId\"}");
                            return;
                        }

                        int bookId = Integer.parseInt(bookIdStr);
                        int memberId = Integer.parseInt(memberIdStr);

                        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
                        IssuedBook ib = new IssuedBook(0, bookId, memberId, sqlDate, null, "Issued");
                        
                        boolean success = issueService.issueBook(ib);
                        if (success) {
                            sendResponse(exchange, 201, "{\"status\":\"success\",\"message\":\"Book issued successfully\"}");
                        } else {
                            sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to issue book\"}");
                        }
                    } else {
                        sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                    }
                    return;
                }

                if (path.endsWith("/return")) {
                    if ("POST".equalsIgnoreCase(method)) {
                        String body = readRequestBody(exchange);
                        Map<String, String> req = ApiHelper.parseJson(body);
                        String issueIdStr = req.get("issueId");

                        if (issueIdStr == null) {
                            sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Missing required field: issueId\"}");
                            return;
                        }

                        int issueId = Integer.parseInt(issueIdStr);
                        boolean success = issueService.returnBook(issueId);
                        if (success) {
                            sendResponse(exchange, 200, "{\"status\":\"success\",\"message\":\"Book returned successfully\"}");
                        } else {
                            sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Failed to return book or already returned\"}");
                        }
                    } else {
                        sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                    }
                    return;
                }

                if ("GET".equalsIgnoreCase(method)) {
                    List<IssuedBook> ibs = issueService.getIssuedBooks();
                    sendResponse(exchange, 200, ApiHelper.issuedBooksToJson(ibs));
                } else {
                    sendResponse(exchange, 405, "{\"status\":\"error\",\"message\":\"Method Not Allowed\"}");
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"status\":\"error\",\"message\":\"Invalid numeric format in request body or parameter\"}");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"status\":\"error\",\"message\":\"" + ApiHelper.escapeJson(e.getMessage()) + "\"}");
            }
        }
    }
}
