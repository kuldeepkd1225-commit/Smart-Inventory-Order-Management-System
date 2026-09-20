package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.OrderDao;
import com.enterprise.sioms.dao.OrderDaoImpl;
import com.enterprise.sioms.dao.OrderItemDao;
import com.enterprise.sioms.dao.OrderItemDaoImpl;
import com.enterprise.sioms.exception.InvalidUserException;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.CartItem;
import com.enterprise.sioms.model.Order;
import com.enterprise.sioms.model.OrderItem;
import com.enterprise.sioms.model.Product;
import com.enterprise.sioms.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class OrderService {

    private final OrderDao orderDao;

    public OrderService() {
        this.orderDao = new OrderDaoImpl();
    }

    public void createOrder(Order order)
            throws InvalidUserException {

        if (order.getCustomerId() <= 0) {
            throw new InvalidUserException(
                    "Invalid customer ID"
            );
        }

        if (order.getTotalAmount() == null ||
                order.getTotalAmount().signum() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid order amount"
            );
        }

        orderDao.save(order);
    }

    public Order getOrderById(int orderId)
            throws ResourceNotFoundException {

        Order order = orderDao.findById(orderId);

        if (order == null) {
            throw new ResourceNotFoundException(
                    "Order not found"
            );
        }

        return order;
    }

    public void checkout(
            int customerId,
            List<CartItem> cartItems
    ) throws InvalidUserException,
            OutOfStockException,
            ResourceNotFoundException,
            SQLException {

        if (customerId <= 0) {
            throw new InvalidUserException(
                    "Invalid customer ID"
            );
        }

        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cart cannot be empty"
            );
        }

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            OrderItemDao orderItemDao =
                    new OrderItemDaoImpl();

            BigDecimal totalAmount =
                    BigDecimal.ZERO;

            for (CartItem item : cartItems) {

                if (item.getQuantity() <= 0) {
                    throw new IllegalArgumentException(
                            "Quantity must be greater than zero"
                    );
                }

                if (item.getPrice() == null) {
                    throw new IllegalArgumentException(
                            "Product price cannot be null"
                    );
                }

                BigDecimal subtotal =
                        item.getPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        );

                totalAmount =
                        totalAmount.add(subtotal);
            }

            Order order = new Order(
                    customerId,
                    totalAmount,
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );

            int orderId =
                    orderDao.save(order, connection);

            for (CartItem item : cartItems) {

                Product product =
                        findProduct(
                                item.getProductId(),
                                connection
                        );

                if (product == null) {
                    throw new ResourceNotFoundException(
                            "Product not found: "
                                    + item.getProductId()
                    );
                }

                if (item.getQuantity()
                        > product.getStockQuantity()) {

                    throw new OutOfStockException(
                            "Insufficient stock for product: "
                                    + item.getProductId()
                    );
                }

                OrderItem orderItem =
                        new OrderItem(
                                orderId,
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice()
                        );

                orderItemDao.save(
                        orderItem,
                        connection
                );

                reduceStock(
                        product,
                        item.getQuantity(),
                        connection
                );
            }

            connection.commit();

        } catch (SQLException |
                 OutOfStockException |
                 ResourceNotFoundException |
                 IllegalArgumentException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }

    private Product findProduct(
            int productId,
            Connection connection
    ) throws SQLException {

        String sql =
                "SELECT product_id, product_name, category, " +
                "price, stock_quantity, updated_at " +
                "FROM products WHERE product_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Product product =
                            new Product();

                    product.setProductId(
                            resultSet.getInt(
                                    "product_id"
                            )
                    );

                    product.setProductName(
                            resultSet.getString(
                                    "product_name"
                            )
                    );

                    product.setCategory(
                            resultSet.getString(
                                    "category"
                            )
                    );

                    product.setPrice(
                            resultSet.getBigDecimal(
                                    "price"
                            )
                    );

                    product.setStockQuantity(
                            resultSet.getInt(
                                    "stock_quantity"
                            )
                    );

                    product.setUpdatedAt(
                            resultSet.getTimestamp(
                                    "updated_at"
                            )
                    );

                    return product;
                }
            }
        }

        return null;
    }

    private void reduceStock(
            Product product,
            int quantity,
            Connection connection
    ) throws SQLException {

        String sql =
                "UPDATE products " +
                "SET stock_quantity = ?, updated_at = ? " +
                "WHERE product_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            int newStock =
                    product.getStockQuantity()
                            - quantity;

            statement.setInt(1, newStock);

            statement.setTimestamp(
                    2,
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );

            statement.setInt(
                    3,
                    product.getProductId()
            );

            statement.executeUpdate();
        }
    }
}