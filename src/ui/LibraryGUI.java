package ui;

import java.awt.*;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Book;
import model.IssuedBook;
import model.Member;
import service.BookService;
import service.IssueBookService;
import service.MemberService;

public class LibraryGUI extends JFrame {

    // BOOK COMPONENTS
    private JTextField txtBookId;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategory;
    private JTextField txtQuantity;

    private JTable bookTable;
    private DefaultTableModel bookModel;

    // MEMBER COMPONENTS
    private JTextField txtMemberId;
    private JTextField txtMemberName;
    private JTextField txtEmail;
    private JTextField txtPhone;

    private JTable memberTable;
    private DefaultTableModel memberModel;

    // ISSUE COMPONENTS
    private JTextField txtIssueBookId;
    private JTextField txtIssueMemberId;
    private JTextField txtIssueId;

    private JTable issueTable;
    private DefaultTableModel issueModel;

    public LibraryGUI() {

        System.out.println("NEW GUI LOADED");

        setTitle("Library Management System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Books", createBookPanel());
        tabs.addTab("Members", createMemberPanel());
        tabs.addTab("Issue / Return", createIssuePanel());

        add(tabs);

        setVisible(true);
        
    }

    private void loadBooks() {

        bookModel.setRowCount(0);

        BookService service =
                new BookService();

        for(Book book :
                service.getAllBooks()) {

            bookModel.addRow(new Object[]{

                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getQuantity()
            });
        }
    }

    private void loadMembers() {

    memberModel.setRowCount(0);

    MemberService service =
            new MemberService();

    for(Member member :
            service.getAllMembers()) {

        memberModel.addRow(new Object[]{

                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getPhone()
        });
    }
}

private void loadIssuedBooks() {

    issueModel.setRowCount(0);

    IssueBookService service =
            new IssueBookService();

    for(IssuedBook book :
            service.getIssuedBooks()) {

        issueModel.addRow(new Object[]{

                book.getIssueId(),
                book.getBookId(),
                book.getMemberId(),
                book.getIssueDate(),
                book.getReturnDate(),
                book.getStatus()
        });
    }
}

    // BOOK TAB
    private JPanel createBookPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));

        txtBookId = new JTextField();
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtCategory = new JTextField();
        txtQuantity = new JTextField();

        form.add(new JLabel("Book ID"));
        form.add(txtBookId);

        form.add(new JLabel("Title"));
        form.add(txtTitle);

        form.add(new JLabel("Author"));
        form.add(txtAuthor);

        form.add(new JLabel("Category"));
        form.add(txtCategory);

        form.add(new JLabel("Quantity"));
        form.add(txtQuantity);

        JPanel buttonPanel = new JPanel();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");
        JButton btnAvailable = new JButton("Available Books");

//         btnSearch.addActionListener(e -> {

//     System.out.println("SEARCH BUTTON CLICKED");

//     JOptionPane.showMessageDialog(
//         this,
//         "Search Button Clicked"
//     );
// });

       btnSearch.addActionListener(e -> {

    String keyword =
            txtTitle.getText();

    bookModel.setRowCount(0);

    BookService service =
            new BookService();

    for(Book book :
            service.searchBook(keyword)) {

        bookModel.addRow(new Object[]{

                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getQuantity()
        });
    }
});

        btnAdd.addActionListener(e -> {

    try {

        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String category = txtCategory.getText();
        int quantity = Integer.parseInt(txtQuantity.getText());

        Book book = new Book();

        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setQuantity(quantity);

        BookService service = new BookService();

        if(service.addBook(book)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Book Added Successfully"
            );

            loadBooks();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Failed to Add Book"
            );
        }

    } catch(NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                null,
                "Enter valid quantity"
        );
    }
});



btnUpdate.addActionListener(e -> {

    try {

        Book book = new Book();

        book.setBookId(
                Integer.parseInt(txtBookId.getText()));

        book.setTitle(txtTitle.getText());
        book.setAuthor(txtAuthor.getText());
        book.setCategory(txtCategory.getText());
        book.setQuantity(
                Integer.parseInt(txtQuantity.getText()));

        BookService service = new BookService();

        if(service.updateBook(book)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Book Updated Successfully");

                    loadBooks();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Update Failed");
        }

    } catch(Exception ex) {

        JOptionPane.showMessageDialog(
                null,
                ex.getMessage());
    }
});

btnDelete.addActionListener(e -> {

    try {

        int id =
                Integer.parseInt(txtBookId.getText());

        BookService service =
                new BookService();

        if(service.deleteBook(id)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Book Deleted Successfully");

                    loadBooks();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Delete Failed");
        }

    } catch(Exception ex) {

        JOptionPane.showMessageDialog(
                null,
                ex.getMessage());
    }
});

btnAvailable.addActionListener(e -> {

    bookModel.setRowCount(0);

    BookService service =
            new BookService();

    for(Book book :
            service.getAvailableBooks()) {

        bookModel.addRow(new Object[]{

                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getQuantity()
        });
    }
});

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnAvailable);

        String columns[] = {
                "ID",
                "Title",
                "Author",
                "Category",
                "Quantity"
        };

        bookModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookModel);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // MEMBER TAB
    private JPanel createMemberPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

        txtMemberId = new JTextField();
        txtMemberName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();

        form.add(new JLabel("Member ID"));
        form.add(txtMemberId);

        form.add(new JLabel("Name"));
        form.add(txtMemberName);

        form.add(new JLabel("Email"));
        form.add(txtEmail);

        form.add(new JLabel("Phone"));
        form.add(txtPhone);

        JPanel buttonPanel = new JPanel();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        btnAdd.addActionListener(e -> {

    Member member = new Member();

    member.setName(txtMemberName.getText());
    member.setEmail(txtEmail.getText());
    member.setPhone(txtPhone.getText());

    MemberService service =
            new MemberService();

    if(service.addMember(member)) {

        JOptionPane.showMessageDialog(
                null,
                "Member Added Successfully");

                loadMembers();
    }
});

btnUpdate.addActionListener(e -> {

    try {

        Member member = new Member();

        member.setMemberId(
                Integer.parseInt(txtMemberId.getText()));

        member.setName(txtMemberName.getText());
        member.setEmail(txtEmail.getText());
        member.setPhone(txtPhone.getText());

        MemberService service =
                new MemberService();

        if(service.updateMember(member)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Member Updated Successfully");

            loadMembers();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Update Failed");
        }

    } catch(Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                ex.getMessage());
    }
});

btnDelete.addActionListener(e -> {

    try {

        int id = Integer.parseInt(txtMemberId.getText());

        MemberService service = new MemberService();

        boolean deleted = service.deleteMember(id);

        System.out.println("Deleted = " + deleted);

        if(deleted) {

            JOptionPane.showMessageDialog(
                    null,
                    "Member Deleted Successfully");

            loadMembers();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Delete Failed");
        }

    } catch(Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                ex.toString());
    }
});

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        String columns[] = {
                "ID",
                "Name",
                "Email",
                "Phone"
        };

        memberModel = new DefaultTableModel(columns, 0);
        memberTable = new JTable(memberModel);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ISSUE RETURN TAB
    private JPanel createIssuePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        txtIssueBookId = new JTextField();
        txtIssueMemberId = new JTextField();
        txtIssueId = new JTextField();

        form.add(new JLabel("Book ID"));
        form.add(txtIssueBookId);

        form.add(new JLabel("Member ID"));
        form.add(txtIssueMemberId);

        form.add(new JLabel("Issue ID"));
        form.add(txtIssueId);

        JPanel buttonPanel = new JPanel();

        JButton btnIssue = new JButton("Issue Book");
        JButton btnReturn = new JButton("Return Book");

        btnIssue.addActionListener(e -> {

    try {

        IssuedBook issue =
                new IssuedBook();

        issue.setBookId(
                Integer.parseInt(
                        txtIssueBookId.getText()));

        issue.setMemberId(
                Integer.parseInt(
                        txtIssueMemberId.getText()));

        issue.setIssueDate(
                new Date(
                        System.currentTimeMillis()));

        IssueBookService service =
                new IssueBookService();

        if(service.issueBook(issue)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Book Issued Successfully");

                    loadIssuedBooks();
        }

    } catch(Exception ex) {

        JOptionPane.showMessageDialog(
                null,
                ex.getMessage());
    }
});

btnReturn.addActionListener(e -> {

    try {

        int issueId =
                Integer.parseInt(
                        txtIssueId.getText());

        IssueBookService service =
                new IssueBookService();

        if(service.returnBook(issueId)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Book Returned Successfully");

                    loadIssuedBooks();
        }

    } catch(Exception ex) {

        JOptionPane.showMessageDialog(
                null,
                ex.getMessage());
    }
});

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);

        String columns[] = {
                "Issue ID",
                "Book ID",
                "Member ID",
                "Issue Date",
                "Return Date",
                "Status"
        };

        issueModel = new DefaultTableModel(columns, 0);
        issueTable = new JTable(issueModel);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(issueTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

}