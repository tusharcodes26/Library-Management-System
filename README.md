# 📚 Library Management System (REST API Service)

A Java-based REST API backend service for managing library operations efficiently. This project is built using Java's built-in **HttpServer** for routing REST requests, **JDBC** for database connectivity, and **MySQL** for data storage. It follows a clean layered architecture using Controller (API), Service, DAO, and Model layers.

---

## 🚀 Features

### 🔐 Admin Authentication
- Secure admin login validate route (`POST /api/login`)
- Username and password verification

### 📖 Book Management
- Add new books (`POST /api/books`)
- Update book details (`PUT /api/books`)
- Delete books (`DELETE /api/books?id=...`)
- Search books by keyword (`GET /api/books/search?keyword=...`)
- View all books (`GET /api/books`)

### 👥 Member Management
- Add library members (`POST /api/members`)
- Update member details (`PUT /api/members`)
- Delete members (`DELETE /api/members?id=...`)
- View all registered members (`GET /api/members`)

### 🔄 Issue & Return Books
- Issue books to members (`POST /api/issues/issue`)
- Return issued books (`POST /api/issues/return`)
- Track issue date and return status (`GET /api/issues`)

---

## 🏗️ Project Architecture

The project follows a layered backend architecture:

```text
LibraryManagementSystem
│
├── LibraryManagementSystem_Postman_Collection.json (Postman API Test Collection)
│
├── src
│   ├── api
│   │   ├── ApiHelper.java (Manual JSON helper/serializer)
│   │   └── ApiServer.java (HttpServer REST controllers & routers)
│   │
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
│   ├── util
│   │   └── DBConnection.java
│   │
│   └── Main.java (Entry point to bootstrap API server)
```

---

## 🛠️ Technologies Used

- Java SE (built-in `com.sun.net.httpserver`)
- JDBC
- MySQL
- Object-Oriented Programming (OOP)
- VS Code

---

## ⚙️ Prerequisites

Before running the project, install:

- Java JDK 8 or higher
- MySQL Server
- MySQL JDBC Driver (already included in `lib/`)

---

## 🗄️ Database Setup

### Create Database

```sql
CREATE DATABASE librarydb;
```

### Create Tables

```sql
CREATE TABLE admin (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100)
);

CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(100),
    quantity INT
);

CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE issued_books (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
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

### Compile

```bash
javac -d bin -cp "lib/*;src" src/**/*.java
```

### Run API Server

```bash
java -cp "bin;lib/*" Main
```
The REST API server will start on port `8080`.

---

## 📬 API Testing

An pre-configured Postman Collection is provided in the root directory:
- [LibraryManagementSystem_Postman_Collection.json](LibraryManagementSystem_Postman_Collection.json)

Import this file into Postman to test all endpoints.

---

## 🎯 Concepts Demonstrated

- REST API Backend Development
- Lightweight HTTP Server implementation in pure Java
- Manual JSON parsing and serialization
- Layered Architecture (separation of concerns)
- JDBC database connectivity
- CRUD operations
- CORS setup for API integration

---

## 👨‍💻 Author

**Tushar Chhabra**

GitHub: https://github.com/tusharcodes26