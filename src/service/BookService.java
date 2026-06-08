package service;

import dao.BookDAO;
import java.util.List;
import model.Book;

public class BookService {

    private BookDAO bookDAO = new BookDAO();

    public boolean addBook(Book book) {

        if(book.getTitle() == null ||
           book.getTitle().trim().isEmpty()) {

            System.out.println("Book title cannot be empty");
            return false;
        }

        if(book.getAuthor() == null ||
           book.getAuthor().trim().isEmpty()) {

            System.out.println("Author cannot be empty");
            return false;
        }

        if(book.getQuantity() <= 0) {

            System.out.println("Quantity must be greater than 0");
            return false;
        }

        System.out.println("Service Called");

        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {

        if(book.getBookId() <= 0) {

            System.out.println("Invalid Book ID");
            return false;
        }

        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(int id) {

        if(id <= 0) {

            System.out.println("Invalid Book ID");
            return false;
        }

        return bookDAO.deleteBook(id);
    }

    public List<Book> getAllBooks() {

        return bookDAO.getAllBooks();
    }

    public List<Book> searchBook(String keyword) {

        return bookDAO.searchBook(keyword);
    }

    public List<Book> getAvailableBooks() {

    return bookDAO.getAvailableBooks();
}
}