package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import util.DBConnection;

public class BookDAO {
    
    //ADD BOOK
    public boolean addBook(Book book){

        String sql = "INSERT INTO books(title,author,category,quantity) VALUES(?,?,?,?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getCategory());
                ps.setInt(4, book.getQuantity());

                return ps.executeUpdate() > 0;
            } catch(SQLException e) {
                e.printStackTrace();
        }
        System.out.println("DAO Called");

        return false;
}


    //UPDATE BOOK
    public boolean updateBook(Book book){
        
        String sql = "UPDATE books SET title=?,author=?,category=?,quantity=? WHERE book_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getCategory());
                ps.setInt(4, book.getQuantity());
                ps.setInt(5, book.getBookId());

                return ps.executeUpdate() > 0;

        } catch(SQLException e) {
            e.printStackTrace();
    }
    return false;
    }

    //DELETE BOOK
    public boolean deleteBook(int bookId){

        String sql = "DELETE FROM books WHERE book_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

                ps.setInt(1, bookId);

                return ps.executeUpdate() > 0;
            } catch(SQLException e){
                e.printStackTrace();
            }
        return false;
    }

    //VIEW ALL BOOKS
    public List<Book> getAllBooks(){

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

                while(rs.next()) {

                    books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("quantity")
                    ));
                }
            } catch(SQLException e){
                e.printStackTrace();
            }

            return books;
    }

    //SEARCH BOOK
   public List<Book> searchBook(
        String keyword) {

    List<Book> books =
            new ArrayList<>();

    String sql =
        "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

    try(Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql)) {

        ps.setString(
                1,
                "%" + keyword + "%");

        ps.setString(
                2,
                "%" + keyword + "%");

        ResultSet rs =
                ps.executeQuery();

        while(rs.next()) {

            books.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("quantity")
            ));
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return books;
}

    public List<Book> getAvailableBooks() {

    List<Book> books =
            new ArrayList<>();

    String sql =
            "SELECT * FROM books WHERE quantity > 0";

    try(Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery()) {

        while(rs.next()) {

            books.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("quantity")
            ));
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return books;
}
}
    