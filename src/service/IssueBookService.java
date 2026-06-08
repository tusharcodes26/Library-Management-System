package service;

import dao.IssuedBookDAO;
import java.util.List;
import model.IssuedBook;

public class IssueBookService {

    private IssuedBookDAO dao =
            new IssuedBookDAO();

    public boolean issueBook(IssuedBook book) {

        if(book.getBookId() <= 0) {

            System.out.println("Invalid Book ID");
            return false;
        }

        if(book.getMemberId() <= 0) {

            System.out.println("Invalid Member ID");
            return false;
        }

        return dao.issueBook(book);
    }

    public boolean returnBook(int issueId) {

        return dao.returnBook(issueId);
    }

    public List<IssuedBook> getIssuedBooks() {

        return dao.getIssuedBooks();
    }
}