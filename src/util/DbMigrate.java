package util;

import java.sql.Connection;
import java.sql.Statement;

public class DbMigrate {
    public static void main(String[] args) {
        System.out.println("=== Starting Database Schema Migration ===");
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            if (conn == null) {
                System.err.println("FAIL: Database connection is null. Please verify your DB connection settings.");
                System.exit(1);
            }

            // 1. Alter books table
            System.out.println("Migrating 'books' table: Adding 'status' column...");
            try {
                stmt.executeUpdate("ALTER TABLE books ADD COLUMN status VARCHAR(20) DEFAULT 'Active'");
                System.out.println("PASS: status column added to books table.");
            } catch (Exception e) {
                System.out.println("NOTE: Books status column already exists or table alter failed: " + e.getMessage());
            }

            // 2. Alter members table
            System.out.println("Migrating 'members' table: Adding 'status' column...");
            try {
                stmt.executeUpdate("ALTER TABLE members ADD COLUMN status VARCHAR(20) DEFAULT 'Active'");
                System.out.println("PASS: status column added to members table.");
            } catch (Exception e) {
                System.out.println("NOTE: Members status column already exists or table alter failed: " + e.getMessage());
            }

            // 3. Initialize existing null status columns to 'Active'
            System.out.println("Initializing status of existing records to 'Active'...");
            int updatedBooks = stmt.executeUpdate("UPDATE books SET status = 'Active' WHERE status IS NULL");
            int updatedMembers = stmt.executeUpdate("UPDATE members SET status = 'Active' WHERE status IS NULL");
            System.out.println("PASS: Initialized " + updatedBooks + " books and " + updatedMembers + " members.");

            System.out.println("=== Database Schema Migration Finished Successfully! ===");
        } catch (Exception e) {
            System.err.println("FAIL: Database schema migration failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
