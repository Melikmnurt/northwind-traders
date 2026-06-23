package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Configure database connection
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername("root");

        // Replace with your actual MySQL password
        dataSource.setPassword("Mohammednur");

        // Display home screen menu
        System.out.println("What do you want to do?");
        System.out.println("  1) Display all products");
        System.out.println("  2) Display all customers");
        System.out.println("  0) Exit");
        System.out.print("Select an option: ");

        // Read user's menu choice
        int choice = scanner.nextInt();

        // Execute the selected option
        if (choice == 1) {
            displayProducts(dataSource);
        }
        else if (choice == 2) {
            displayCustomers(dataSource);
        }
        else if (choice == 0) {
            System.out.println("Goodbye!");
        }
        else {
            System.out.println("Invalid option.");
        }

        scanner.close();
    }

    // Display all products from the Products table
    public static void displayProducts(BasicDataSource dataSource) {

        // SQL query to retrieve product information
        String sql =
                "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                        "FROM products";

        // Automatically close resources when finished
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {

            // Process each row returned by the query
            while (results.next()) {

                // Read values from the current row
                int productId = results.getInt("ProductID");
                String productName = results.getString("ProductName");
                double unitPrice = results.getDouble("UnitPrice");
                int unitsInStock = results.getInt("UnitsInStock");

                // Display product information
                System.out.println("Product Id: " + productId);
                System.out.println("Name:       " + productName);
                System.out.printf("Price:      %.2f%n", unitPrice);
                System.out.println("Stock:      " + unitsInStock);
                System.out.println("--------------------");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all customers ordered by country
    public static void displayCustomers(BasicDataSource dataSource) {

        // SQL query to retrieve customer information
        String sql =
                "SELECT ContactName, CompanyName, City, Country, Phone " +
                        "FROM customers " +
                        "ORDER BY Country";

        // Automatically close resources when finished
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {

            // Process each customer returned by the query
            while (results.next()) {

                // Read values from the current row
                String contactName = results.getString("ContactName");
                String companyName = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phone = results.getString("Phone");

                // Display customer information
                System.out.println("Contact: " + contactName);
                System.out.println("Company: " + companyName);
                System.out.println("City:    " + city);
                System.out.println("Country: " + country);
                System.out.println("Phone:   " + phone);
                System.out.println("--------------------");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}