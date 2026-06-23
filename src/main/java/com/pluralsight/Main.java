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
        dataSource.setPassword("Mohammednur");

        // Display home screen menu
        System.out.println("What do you want to do?");
        System.out.println("  1) Display all products");
        System.out.println("  2) Display all customers");
        System.out.println("  3) Display all categories");
        System.out.println("  0) Exit");
        System.out.print("Select an option: ");

        // Read user choice as text so injection test input does not crash
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            displayProducts(dataSource);
        }
        else if (choice.equals("2")) {
            displayCustomers(dataSource);
        }
        else if (choice.equals("3")) {
            displayCategories(dataSource, scanner);
        }
        else if (choice.equals("0")) {
            System.out.println("Goodbye!");
        }
        else {
            System.out.println("Invalid option.");
        }

        scanner.close();
    }

    // Display all products
    public static void displayProducts(BasicDataSource dataSource) {

        String sql = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {
                System.out.println("Product Id: " + results.getInt("ProductID"));
                System.out.println("Name:       " + results.getString("ProductName"));
                System.out.printf("Price:      %.2f%n", results.getDouble("UnitPrice"));
                System.out.println("Stock:      " + results.getInt("UnitsInStock"));
                System.out.println("--------------------");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all customers ordered by country
    public static void displayCustomers(BasicDataSource dataSource) {

        String sql = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY Country";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {
                System.out.println("Contact: " + results.getString("ContactName"));
                System.out.println("Company: " + results.getString("CompanyName"));
                System.out.println("City:    " + results.getString("City"));
                System.out.println("Country: " + results.getString("Country"));
                System.out.println("Phone:   " + results.getString("Phone"));
                System.out.println("--------------------");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display categories, ask for category ID, then display products in that category
    public static void displayCategories(BasicDataSource dataSource, Scanner scanner) {

        String categorySql = "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement categoryStatement = connection.prepareStatement(categorySql);
                ResultSet categoryResults = categoryStatement.executeQuery()
        ) {
            System.out.println("Categories");
            System.out.println("--------------------");

            while (categoryResults.next()) {
                System.out.println(categoryResults.getInt("CategoryID") + ") "
                        + categoryResults.getString("CategoryName"));
            }

            System.out.print("Enter a category ID: ");
            String categoryId = scanner.nextLine();

            // The ? placeholder keeps user input safe
            String productSql =
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                            "FROM products " +
                            "WHERE CategoryID = ?";

            try (
                    PreparedStatement productStatement = connection.prepareStatement(productSql)
            ) {
                productStatement.setString(1, categoryId);

                try (ResultSet productResults = productStatement.executeQuery()) {
                    while (productResults.next()) {
                        System.out.println("Product Id: " + productResults.getInt("ProductID"));
                        System.out.println("Name:       " + productResults.getString("ProductName"));
                        System.out.printf("Price:      %.2f%n", productResults.getDouble("UnitPrice"));
                        System.out.println("Stock:      " + productResults.getInt("UnitsInStock"));
                        System.out.println("--------------------");
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}