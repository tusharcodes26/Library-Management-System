package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.IssuedBook;
import util.DBConnection;

public class IssuedBookDAO {
    public boolean issueBook(IssuedBook issuedBook) {

    String sql =
            "INSERT INTO issued_books(book_id,member_id,issue_date,status) VALUES(?,?,?,?)";

    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, issuedBook.getBookId());
        ps.setInt(2, issuedBook.getMemberId());
        ps.setDate(3, issuedBook.getIssueDate());
        ps.setString(4, "Issued");

        return ps.executeUpdate() > 0;

    } catch(SQLException e) {
        e.printStackTrace();
    }

    return false;
}

public boolean returnBook(int issueId) {

    String sql =
            "UPDATE issued_books SET return_date=?, status=? WHERE issue_id=?";

    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDate(1, new Date(System.currentTimeMillis()));
        ps.setString(2, "Returned");
        ps.setInt(3, issueId);

        return ps.executeUpdate() > 0;

    } catch(SQLException e) {
        e.printStackTrace();
    }

    return false;
}

public List<IssuedBook> getIssuedBooks() {

    List<IssuedBook> list =
            new ArrayList<>();

    String sql =
            "SELECT * FROM issued_books WHERE status='Issued'";

    try(Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery()) {

        while(rs.next()) {

            IssuedBook book =
                    new IssuedBook();

            book.setIssueId(
                    rs.getInt("issue_id"));

            book.setBookId(
                    rs.getInt("book_id"));

            book.setMemberId(
                    rs.getInt("member_id"));

            book.setIssueDate(
                    rs.getDate("issue_date"));

            book.setReturnDate(
                    rs.getDate("return_date"));

            book.setStatus(
                    rs.getString("status"));

            list.add(book);
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return list;
}
}
