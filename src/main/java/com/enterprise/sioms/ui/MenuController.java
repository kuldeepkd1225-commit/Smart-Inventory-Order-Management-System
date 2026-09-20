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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuController {

    private final Scanner scanner;
    private final AuthService authService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    private User loggedInUser;

    public MenuController() {
        scanner = new Scanner(System.in);
        authService = new AuthService();
        inventoryService = new InventoryService();
        orderService = new OrderService();
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("   SMART INVENTORY & ORDER SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    register();
                    break;

                case "2":
                    login();
                    break;

                case "3":
                    running = false;

                    System.out.println(
                            "Thank you for using SIOMS."
                    );

                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }

    private void register() {

        System.out.println();
        System.out.println(
                "========== USER REGISTRATION =========="
        );

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print(
                "Enter role (ADMIN/CUSTOMER): "
        );

        String role = scanner.nextLine();

        try {

            User user = authService.register(
                    name,
                    email,
                    password,
                    role
            );

            System.out.println();
            System.out.println(
                    "Registration successful."
            );

            System.out.println(
                    "User ID: " + user.getUserId()
            );

            System.out.println(
                    "Name: " + user.getName()
            );

            System.out.println(
                    "Email: " + user.getEmail()
            );

            System.out.println(
                    "Role: " + user.getRole()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Registration failed: "
                            + e.getMessage()
            );
        }
    }

    private void login() {

        System.out.println();
        System.out.println(
                "=============== LOGIN ==============="
        );

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {

            loggedInUser =
                    authService.login(
                            email,
                            password
                    );

            System.out.println();
            System.out.println(
                    "Login successful."
            );

            System.out.println(
                    "Welcome, "
                            + loggedInUser.getName()
            );

            if (loggedInUser.getRole()
                    .equalsIgnoreCase("ADMIN")) {

                showAdminMenu();

            } else {

                showCustomerMenu();
            }

        } catch (InvalidUserException e) {

            System.out.println(
                    "Login failed: "
                            + e.getMessage()
            );
        }
    }

    private void showCustomerMenu() {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println();
            System.out.println(
                    "===================================="
            );
            System.out.println(
                    "           CUSTOMER MENU"
            );
            System.out.println(
                    "===================================="
            );

            System.out.println("1. View Profile");
            System.out.println("2. View Products");
            System.out.println("3. Checkout");
            System.out.println("4. Logout");

            System.out.println(
                    "===================================="
            );

            System.out.print(
                    "Enter your choice: "
            );

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    showProfile();
                    break;

                case "2":
                    viewProducts();
                    break;

                case "3":
                    checkout();
                    break;

                case "4":

                    loggedInUser = null;
                    loggedIn = false;

                    System.out.println(
                            "Logged out successfully."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void showAdminMenu() {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println();
            System.out.println(
                    "===================================="
            );
            System.out.println(
                    "             ADMIN MENU"
            );
            System.out.println(
                    "===================================="
            );

            System.out.println("1. View Profile");
            System.out.println("2. View Products");
            System.out.println("3. Add Product");
            System.out.println("4. Update Product");
            System.out.println("5. Logout");

            System.out.println(
                    "===================================="
            );

            System.out.print(
                    "Enter your choice: "
            );

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    showProfile();
                    break;

                case "2":
                    viewProducts();
                    break;

                case "3":
                    addProduct();
                    break;

                case "4":
                    updateProduct();
                    break;

                case "5":

                    loggedInUser = null;
                    loggedIn = false;

                    System.out.println(
                            "Logged out successfully."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private void showProfile() {

        if (loggedInUser == null) {
            return;
        }

        System.out.println();
        System.out.println(
                "========== PROFILE =========="
        );

        System.out.println(
                "User ID: "
                        + loggedInUser.getUserId()
        );

        System.out.println(
                "Name: "
                        + loggedInUser.getName()
        );

        System.out.println(
                "Email: "
                        + loggedInUser.getEmail()
        );

        System.out.println(
                "Role: "
                        + loggedInUser.getRole()
        );
    }

    private void addProduct() {

        System.out.println();
        System.out.println(
                "========== ADD PRODUCT =========="
        );

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter price: ");
        String priceInput = scanner.nextLine();

        BigDecimal price;

        try {

            price = new BigDecimal(priceInput);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid price."
            );

            return;
        }

        System.out.print("Enter initial stock: ");
        String stockInput = scanner.nextLine();

        int stockQuantity;

        try {

            stockQuantity =
                    Integer.parseInt(stockInput);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid stock quantity."
            );

            return;
        }

        Product product =
                new Product(
                        productName,
                        category,
                        price,
                        stockQuantity,
                        null
                );

        try {

            inventoryService.addProduct(product);

            System.out.println();
            System.out.println(
                    "Product added successfully."
            );

        } catch (ResourceNotFoundException e) {

            System.out.println(
                    "Product could not be added: "
                            + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Product could not be added: "
                            + e.getMessage()
            );
        }
    }

    private void updateProduct() {

        System.out.println();
        System.out.println(
                "========== UPDATE PRODUCT =========="
        );

        System.out.print("Enter product ID: ");

        String productIdInput =
                scanner.nextLine();

        int productId;

        try {

            productId =
                    Integer.parseInt(
                            productIdInput
                    );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid product ID."
            );

            return;
        }

        Product existingProduct;

        try {

            existingProduct =
                    inventoryService.getProductById(
                            productId
                    );

        } catch (ResourceNotFoundException e) {

            System.out.println(
                    "Product not found."
            );

            return;
        }

        System.out.println(
                "Current product:"
        );

        System.out.println(
                "Name: "
                        + existingProduct.getProductName()
        );

        System.out.println(
                "Category: "
                        + existingProduct.getCategory()
        );

        System.out.println(
                "Price: "
                        + existingProduct.getPrice()
        );

        System.out.println(
                "Stock: "
                        + existingProduct.getStockQuantity()
        );

        System.out.print(
                "Enter new product name: "
        );

        String productName =
                scanner.nextLine();

        System.out.print(
                "Enter new category: "
        );

        String category =
                scanner.nextLine();

        System.out.print(
                "Enter new price: "
        );

        String priceInput =
                scanner.nextLine();

        BigDecimal price;

        try {

            price =
                    new BigDecimal(
                            priceInput
                    );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid price."
            );

            return;
        }

        System.out.print(
                "Enter new stock: "
        );

        String stockInput =
                scanner.nextLine();

        int stockQuantity;

        try {

            stockQuantity =
                    Integer.parseInt(
                            stockInput
                    );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid stock quantity."
            );

            return;
        }

        Product updatedProduct =
                new Product(
                        productName,
                        category,
                        price,
                        stockQuantity,
                        null
                );

        updatedProduct.setProductId(
                productId
        );

        try {

            inventoryService.updateProduct(
                    updatedProduct
            );

            System.out.println();
            System.out.println(
                    "Product updated successfully."
            );

        } catch (ResourceNotFoundException e) {

            System.out.println(
                    "Product not found."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Product could not be updated: "
                            + e.getMessage()
            );
        }
    }

    private void viewProducts() {

        System.out.println();
        System.out.println(
                "========== PRODUCT CATALOG =========="
        );

        System.out.println(
                "1. View all available products"
        );

        System.out.println(
                "2. Filter by category"
        );

        System.out.println("3. Back");

        System.out.println(
                "===================================="
        );

        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine();

        switch (choice) {

            case "1":
                displayProducts(
                        inventoryService
                                .getAvailableProducts()
                );
                break;

            case "2":

                System.out.print(
                        "Enter category: "
                );

                String category =
                        scanner.nextLine();

                try {

                    displayProducts(
                            inventoryService
                                    .getProductsByCategory(
                                            category
                                    )
                    );

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Invalid category: "
                                    + e.getMessage()
                    );
                }

                break;

            case "3":
                break;

            default:
                System.out.println(
                        "Invalid choice. Please try again."
                );
        }
    }

    private void displayProducts(
            List<Product> products
    ) {

        System.out.println();

        if (products.isEmpty()) {

            System.out.println(
                    "No available products found."
            );

            return;
        }

        System.out.println(
                "------------- PRODUCTS -------------"
        );

        for (Product product : products) {

            System.out.println(
                    "Product ID: "
                            + product.getProductId()
            );

            System.out.println(
                    "Name: "
                            + product.getProductName()
            );

            System.out.println(
                    "Category: "
                            + product.getCategory()
            );

            System.out.println(
                    "Price: "
                            + product.getPrice()
            );

            System.out.println(
                    "Stock: "
                            + product.getStockQuantity()
            );

            System.out.println(
                    "------------------------------------"
            );
        }
    }

    private void checkout() {

        if (loggedInUser == null) {

            System.out.println(
                    "Please login first."
            );

            return;
        }

        List<CartItem> cartItems =
                new ArrayList<>();

        System.out.println();
        System.out.println(
                "========== CHECKOUT =========="
        );

        while (true) {

            System.out.print(
                    "Enter product ID (0 to finish): "
            );

            String productIdInput =
                    scanner.nextLine();

            int productId;

            try {

                productId =
                        Integer.parseInt(
                                productIdInput
                        );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid numeric product ID."
                );

                continue;
            }

            if (productId == 0) {
                break;
            }

            try {

                Product product =
                        inventoryService.getProductById(
                                productId
                        );

                System.out.println(
                        "Product: "
                                + product.getProductName()
                );

                System.out.println(
                        "Price: "
                                + product.getPrice()
                );

                System.out.println(
                        "Available stock: "
                                + product.getStockQuantity()
                );

                System.out.print(
                        "Enter quantity: "
                );

                String quantityInput =
                        scanner.nextLine();

                int quantity;

                try {

                    quantity =
                            Integer.parseInt(
                                    quantityInput
                            );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Please enter a valid quantity."
                    );

                    continue;
                }

                if (quantity <= 0) {

                    System.out.println(
                            "Quantity must be greater than zero."
                    );

                    continue;
                }

                CartItem cartItem =
                        new CartItem(
                                productId,
                                quantity,
                                product.getPrice()
                        );

                cartItems.add(cartItem);

                System.out.println(
                        "Product added to cart."
                );

            } catch (ResourceNotFoundException e) {

                System.out.println(
                        "Product not found."
                );
            }
        }

        if (cartItems.isEmpty()) {

            System.out.println(
                    "Cart is empty. Checkout cancelled."
            );

            return;
        }

        try {

            orderService.checkout(
                    loggedInUser.getUserId(),
                    cartItems
            );

            System.out.println();
            System.out.println(
                    "========== ORDER SUCCESSFUL =========="
            );

            System.out.println(
                    "Checkout completed successfully."
            );

            System.out.println(
                    "======================================"
            );

        } catch (InvalidUserException e) {

            System.out.println(
                    "Checkout failed: "
                            + e.getMessage()
            );

        } catch (OutOfStockException e) {

            System.out.println(
                    "Checkout failed: "
                            + e.getMessage()
            );

            System.out.println(
                    "Transaction cancelled."
            );

        } catch (ResourceNotFoundException e) {

            System.out.println(
                    "Checkout failed: "
                            + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error during checkout: "
                            + e.getMessage()
            );

            System.out.println(
                    "Transaction cancelled."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Checkout failed: "
                            + e.getMessage()
            );
        }
    }
}