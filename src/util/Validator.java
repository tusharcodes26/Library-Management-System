package util;

import model.Book;
import model.Member;

public class Validator {
    
    public static void validateBook(Book book) throws ValidationException {
        if (book == null) {
            throw new ValidationException("Book data is required");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ValidationException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new ValidationException("Author cannot be empty");
        }
        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            throw new ValidationException("Category cannot be empty");
        }
        if (book.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
    }

    public static void validateMember(Member member) throws ValidationException {
        if (member == null) {
            throw new ValidationException("Member data is required");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new ValidationException("Member name cannot be empty");
        }
        if (member.getEmail() == null || !member.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
        if (member.getPhone() == null || !member.getPhone().matches("^\\+?[0-9\\s\\-]{8,15}$")) {
            throw new ValidationException("Invalid phone number format");
        }
    }
}
