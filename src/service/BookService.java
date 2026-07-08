package service;

import dao.BookDAO;
import java.util.List;
import model.Book;

public class BookService {

    private BookDAO bookDAO = new BookDAO();

    public boolean addBook(Book book) {
        util.Validator.validateBook(book);
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        if(book.getBookId() <= 0) {
            throw new util.ValidationException("Invalid Book ID");
        }
        util.Validator.validateBook(book);
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