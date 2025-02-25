# Library Management System

## Overview

This project presents a robust **Library Management System** developed using Java and MySQL. The system provides seamless management of library operations with the following core functionalities:

- **Add new books** to the library's inventory.
- **View all available books** in the collection.
- **Borrow books** by updating their availability status.
- **Return books** and mark them as available.
- **Exit the application** with automatic data persistence in the database.

## Key Features

- **Real-time database interaction** powered by MySQL.
- **Intuitive console-based interface** for user interaction.
- **Robust error handling** for invalid inputs and SQL exceptions.
- **Data persistence** through secure CRUD operations directly on the database.

## Technologies Used

- **Java** (JDK 17 or higher)
- **MySQL** (Ensure MySQL server is active)
- **JDBC** for reliable database connectivity

## Database Configuration

1. Establish a MySQL database named `library_db`:

```sql
CREATE DATABASE library_db;
```

2. Define the `books` table structure:

```sql
USE library_db;
CREATE TABLE books (
    id INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    isAvailable BOOLEAN
);
```

3. Optionally, populate the table with sample data:

```sql
INSERT INTO books (id, title, author, isAvailable) VALUES
(1, 'Effective Java', 'Joshua Bloch', true),
(2, 'Clean Code', 'Robert C. Martin', true),
(3, 'Head First Java', 'Kathy Sierra', true);
```

## Project Setup & Execution

1. Clone or download the project repository.
2. Import the project into your preferred IDE.
3. Ensure the MySQL server is running and the database is correctly configured.
4. Update the database credentials in `LibraryManagementSystem.java`:

```java
static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
static final String DB_USER = "root";
static final String DB_PASSWORD = "your_password";
```

5. Compile and execute the program:

```bash
javac LibraryManagementSystem.java
java project.LibraryManagementSystem
```

## Usage Guide

- **Add a Book:** Provide the book's ID, title, and author.
- **Display Books:** View all listed books along with their availability status.
- **Borrow a Book:** Input the book's ID to mark it as borrowed.
- **Return a Book:** Enter the book's ID to restore its availability.
- **Exit the Application:** Safely close the program while saving data.

## License

This project is licensed under the MIT License. Viewing and usage are permitted, but modifications are restricted.

---

Explore and utilize this project to enhance your understanding of Java-based database applications.

