package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class Book {
    int id;
    String title;
    String author;
    boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }
    @Override
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Author: " + author + ", Available: " + isAvailable;
    }
}
public class LibraryManagementSystem {
    static ArrayList<Book> books = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    // Database connection details
    static final String DB_URL = "jdbc:mysql://localhost:3306/library_db"; // Replace with your database URL
    static final String DB_USER = "root"; // Replace with your database username
    static final String DB_PASSWORD = "password"; // Replace with your database password
    public static void main(String[] args) {
        loadFromDatabase(); // Load saved data from the database at the start
        while (true) {
            System.out.println("\n1. Add Book\n2. Display Books\n3. Borrow Book\n4. Return Book\n5. Exit");
            int choice = getValidInteger("Enter your choice: ");
            switch (choice) {
                case 1 -> addBook();
                case 2 -> displayBooks();
                case 3 -> borrowBook();
                case 4 -> returnBook();
                case 5 -> {
                    saveToDatabase(); // Save data to the database before exiting
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
    public static void addBook() {
        int id = getValidInteger("Enter Book ID: ");
        if (isIdUnique(id)) {
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Author Name: ");
            String author = scanner.nextLine();
            // Insert into database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "INSERT INTO books (id, title, author, isAvailable) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, id);
                stmt.setString(2, title);
                stmt.setString(3, author);
                stmt.setBoolean(4, true); // Default availability
                stmt.executeUpdate();
                System.out.println("Book added successfully!");
            } catch (SQLException e) {
                System.out.println("Error adding book to database: " + e.getMessage());
            }
        } else {
            System.out.println("Book ID already exists. Please use a unique ID.");
        }
    }
    public static void displayBooks() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            boolean isEmpty = true;
            while (rs.next()) {
                isEmpty = false;
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean isAvailable = rs.getBoolean("isAvailable");
                System.out.println(new Book(id, title, author).toString().replace("true", "Yes").replace("false", "No"));
            }
            if (isEmpty) {
                System.out.println("No books in the library.");
            }
        } catch (SQLException e) {
            System.out.println("Error loading books from database: " + e.getMessage());
        }
    }
    public static void borrowBook() {
        int id = getValidInteger("Enter Book ID to borrow: ");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE books SET isAvailable = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Book not found or already borrowed.");
            }
        } catch (SQLException e) {
            System.out.println("Error borrowing book: " + e.getMessage());
        }
    }
    public static void returnBook() {
        int id = getValidInteger("Enter Book ID to return: ");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE books SET isAvailable = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, true);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Book not found or was not borrowed.");
            }
        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }
    public static void saveToDatabase() {
        // No need to explicitly save since all operations are directly handled in the database
        System.out.println("Data is already saved in the database.");
    }
    public static void loadFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean isAvailable = rs.getBoolean("isAvailable");
                Book book = new Book(id, title, author);
                book.isAvailable = isAvailable;
                books.add(book);
            }
            System.out.println("Data loaded successfully from the database!");
        } catch (SQLException e) {
            System.out.println("Error loading data from database: " + e.getMessage());
        }
    }
    private static boolean isIdUnique(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) AS count FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking ID uniqueness: " + e.getMessage());
        }
        return false;
    }
    private static int getValidInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
