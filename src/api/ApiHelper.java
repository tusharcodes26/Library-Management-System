package api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import model.Book;
import model.Member;
import model.IssuedBook;

public class ApiHelper {

    public static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return map;
        }
        // Match keys and string or primitive values
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(?:\"([^\"]*)\"|([^,{}]+))");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3).trim();
            map.put(key, value);
        }
        return map;
    }

    public static String escapeJson(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '\"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String bookToJson(Book book) {
        return String.format(
            "{\"bookId\":%d,\"title\":\"%s\",\"author\":\"%s\",\"category\":\"%s\",\"quantity\":%d}",
            book.getBookId(),
            escapeJson(book.getTitle()),
            escapeJson(book.getAuthor()),
            escapeJson(book.getCategory()),
            book.getQuantity()
        );
    }

    public static String booksToJson(List<Book> books) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < books.size(); i++) {
            sb.append(bookToJson(books.get(i)));
            if (i < books.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String memberToJson(Member member) {
        return String.format(
            "{\"memberId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}",
            member.getMemberId(),
            escapeJson(member.getName()),
            escapeJson(member.getEmail()),
            escapeJson(member.getPhone())
        );
    }

    public static String membersToJson(List<Member> members) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < members.size(); i++) {
            sb.append(memberToJson(members.get(i)));
            if (i < members.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String issuedBookToJson(IssuedBook ib) {
        return String.format(
            "{\"issueId\":%d,\"bookId\":%d,\"memberId\":%d,\"issueDate\":\"%s\",\"returnDate\":\"%s\",\"status\":\"%s\"}",
            ib.getIssueId(),
            ib.getBookId(),
            ib.getMemberId(),
            ib.getIssueDate() != null ? ib.getIssueDate().toString() : "",
            ib.getReturnDate() != null ? ib.getReturnDate().toString() : "",
            escapeJson(ib.getStatus())
        );
    }

    public static String issuedBooksToJson(List<IssuedBook> ibs) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < ibs.size(); i++) {
            sb.append(issuedBookToJson(ibs.get(i)));
            if (i < ibs.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
