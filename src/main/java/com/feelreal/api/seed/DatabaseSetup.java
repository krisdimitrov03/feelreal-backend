package com.feelreal.api.seed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseSetup {

    @Value("${spring.datasource.url}")
    private String DATABASE_URL;

    @Value("${spring.datasource.username}")
    private String DB_USER;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    public void createDatabaseAndTable() {
        // SQL commands
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS userdb";
        String useDatabaseSQL = "USE userdb";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255))";

        // Try-with-resources to automatically close the connection
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Execute SQL commands to create database and use it
            stmt.execute(createDatabaseSQL);  // Create database
            stmt.execute(useDatabaseSQL);     // Switch to the new database
            stmt.execute(createTableSQL);     // Create table within the new database

            System.out.println("Database and User table created successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while creating the database or table.");
        }
    }
}
