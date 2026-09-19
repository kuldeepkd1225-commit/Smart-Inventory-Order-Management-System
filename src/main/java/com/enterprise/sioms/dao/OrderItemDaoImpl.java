package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.OrderItem;
import com.enterprise.sioms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {

    @Override
    public void save(OrderItem orderItem) {

        String sql = "INSERT INTO order_items " +
                "(order_id, product_id, quantity, subtotal) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setBigDecimal(4, orderItem.getSubtotal());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderItem findById(int id) {

        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRowToOrderItem(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderItem> findAll() {

        String sql = "SELECT * FROM order_items";

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                orderItems.add(mapRowToOrderItem(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    @Override
    public void update(OrderItem orderItem) {

        String sql = "UPDATE order_items SET " +
                "order_id = ?, product_id = ?, quantity = ?, subtotal = ? " +
                "WHERE order_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setBigDecimal(4, orderItem.getSubtotal());
            statement.setInt(5, orderItem.getOrderItemId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(OrderItem orderItem, Connection connection)
            throws SQLException {

        String sql = "INSERT INTO order_items " +
                "(order_id, product_id, quantity, subtotal) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setBigDecimal(4, orderItem.getSubtotal());

            statement.executeUpdate();
        }
    }

    private OrderItem mapRowToOrderItem(ResultSet resultSet)
            throws SQLException {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderItemId(
                resultSet.getInt("order_item_id")
        );

        orderItem.setOrderId(
                resultSet.getInt("order_id")
        );

        orderItem.setProductId(
                resultSet.getInt("product_id")
        );

        orderItem.setQuantity(
                resultSet.getInt("quantity")
        );

        orderItem.setSubtotal(
                resultSet.getBigDecimal("subtotal")
        );

        return orderItem;
    }
}