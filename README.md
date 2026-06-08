# 📚 Library Management System

A Java-based desktop application for managing library operations efficiently. This project is built using **Java Swing** for the graphical user interface, **JDBC** for database connectivity, and **MySQL** for data storage. It follows a layered architecture using DAO, Service, Model, and UI components.

## 🚀 Features

### 🔐 Admin Authentication
- Secure admin login system
- Username and password validation
- Access control before entering the system

### 📖 Book Management
- Add new books
- Update book details
- Delete books
- Search books
- View all books
- Track available quantities

### 👥 Member Management
- Add library members
- Update member information
- Delete members
- View all registered members

### 🔄 Issue & Return Books
- Issue books to members
- Return issued books
- Track issue date and return date
- Monitor book status

### 🖥️ User-Friendly Interface
- Developed using Java Swing
- Tab-based navigation
- Interactive tables for displaying records
- Popup notifications for operations

---

## 🏗️ Project Architecture

The project follows a layered architecture:

```text
LibraryManagementSystem
│
├── src
│   ├── dao
│   │   ├── AdminDAO.java
│   │   ├── BookDAO.java
│   │   ├── MemberDAO.java
│   │   └── IssuedBookDAO.java
│   │
│   ├── model
│   │   ├── Admin.java
│   │   ├── Book.java
│   │   ├── Member.java
│   │   └── IssuedBook.java
│   │
│   ├── service
│   │   ├── AdminService.java
│   │   ├── BookService.java
│   │   ├── MemberService.java
│   │   └── IssueBookService.java
│   │
│   ├── ui
│   │   ├── LoginFrame.java
│   │   └── LibraryGUI.java
│   │
│   ├── util
│   │   └── DBConnection.java
│   │
│   └── Main.java
```

---

## 🛠️ Technologies Used

- Java
- Java Swing
- JDBC
- MySQL
- Object-Oriented Programming (OOP)
- VS Code

---

## ⚙️ Prerequisites

Before running the project, install:

- Java JDK 8 or higher
- MySQL Server
- MySQL JDBC Driver
- VS Code / IntelliJ IDEA / Eclipse

---

## 🗄️ Database Setup

### Create Database

```sql
CREATE DATABASE librarydb;
```

### Suggested Tables

#### Admin

```sql
CREATE TABLE admin (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100)
);
```

#### Books

```sql
CREATE TABLE books (
    book_id INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(100),
    quantity INT
);
```

#### Members

```sql
CREATE TABLE members (
    member_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);
```

#### Issued Books

```sql
CREATE TABLE issued_books (
    issue_id INT PRIMARY KEY,
    book_id INT,
    member_id INT,
    issue_date DATE,
    return_date DATE,
    status VARCHAR(20)
);
```

Update the database credentials inside `DBConnection.java`.

```java
String url = "jdbc:mysql://localhost:3306/librarydb";
String user = "root";
String password = "your_password";
```

---

## ▶️ How to Run

### Clone Repository

```bash
git clone https://github.com/tusharcodes26/Library-Management-System.git
```

### Open Project

```bash
cd Library-Management-System
```

### Compile

```bash
javac -d bin src/**/*.java
```

### Run

```bash
java Main
```

---

## 📸 Application Modules

### Login Module
- Admin authentication
- Secure access to the system

### Books Module
- Add, update, delete, search books
- Display all books

### Members Module
- Manage member records
- Update and remove members

### Issue/Return Module
- Issue books
- Return books
- Track issued book history

---

## 🎯 Concepts Demonstrated

- Object-Oriented Programming
- Encapsulation
- Layered Architecture
- JDBC Database Connectivity
- CRUD Operations
- Swing GUI Development
- Exception Handling
- Event Handling

---

## 🔮 Future Enhancements

- Role-based authentication
- Fine calculation system
- Book reservation feature
- Due-date reminders
- Export reports to PDF/Excel
- Dashboard analytics
- Barcode/QR code integration

---

## 👨‍💻 Author

**Tushar Chhabra**

GitHub: https://github.com/tusharcodes26

---

## ⭐ Support

If you found this project helpful, please consider starring the repository.

⭐ Star the project on GitHub and feel free to contribute!