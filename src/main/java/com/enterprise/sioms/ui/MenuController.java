package com.enterprise.sioms.ui;

import com.enterprise.sioms.exception.InvalidUserException;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.CartItem;
import com.enterprise.sioms.model.Product;
import com.enterprise.sioms.model.User;
import com.enterprise.sioms.service.AuthService;
import com.enterprise.sioms.service.InventoryService;
import com.enterprise.sioms.service.OrderService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuController {

    private final Scanner scanner;
    private final AuthService authService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public MenuController() {
        scanner = new Scanner(System.in);
        authService = new AuthService();
        inventoryService = new InventoryService();
        orderService = new OrderService();
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== SMART INVENTORY & ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    login();
                    break;

                case 2:
                    running = false;
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private void login() {

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {

            User user = authService.login(email, password);

            System.out.println("\nLogin successful.");
            System.out.println("Welcome, " + user.getName());

            showUserMenu(user);

        } catch (InvalidUserException e) {

            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void showUserMenu(User user) {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println("\n===== USER MENU =====");
            System.out.println("1. View Profile");
            System.out.println("2. Checkout");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    showProfile(user);
                    break;

                case 2:
                    checkout(user);
                    break;

                case 3:
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showProfile(User user) {

        System.out.println("\n===== PROFILE =====");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
    }

    private void checkout(User user) {

        List<CartItem> cartItems = new ArrayList<>();

        System.out.print("\nEnter number of products: ");
        int productCount = scanner.nextInt();
        scanner.nextLine();

        if (productCount <= 0) {
            System.out.println("Number of products must be greater than zero.");
            return;
        }

        for (int i = 0; i < productCount; i++) {

            System.out.println("\nProduct " + (i + 1));

            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            try {

                Product product = inventoryService.getProductById(productId);

                System.out.println("Product: " + product.getProductName());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Available stock: " + product.getStockQuantity());

                CartItem cartItem =
                        new CartItem(
                                productId,
                                quantity,
                                product.getPrice()
                        );

                cartItems.add(cartItem);

            } catch (ResourceNotFoundException e) {

                System.out.println("Product not found.");
                return;
            }
        }

        try {

            orderService.checkout(
                    user.getUserId(),
                    cartItems
            );

            System.out.println("\nCheckout completed successfully.");

        } catch (InvalidUserException e) {

            System.out.println("Invalid customer.");

        } catch (OutOfStockException e) {

            System.out.println("Checkout failed: " + e.getMessage());

        } catch (ResourceNotFoundException e) {

            System.out.println("Checkout failed: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());
        }
    }
}