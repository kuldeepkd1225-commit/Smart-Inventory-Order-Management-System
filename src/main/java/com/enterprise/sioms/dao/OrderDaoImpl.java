package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Order;
import com.enterprise.sioms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public void save(Order order) {

        String sql = "INSERT INTO orders (customer_id, total_amount, order_date) " +
                     "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, order.getCustomerId());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setTimestamp(3, order.getOrderDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order findById(int id) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRowToOrder(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> findAll() {

        String sql = "SELECT * FROM orders";

        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public void update(Order order) {

        String sql = "UPDATE orders SET customer_id = ?, " +
                     "total_amount = ?, order_date = ? " +
                     "WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, order.getCustomerId());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setTimestamp(3, order.getOrderDate());
            statement.setInt(4, order.getOrderId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order mapRowToOrder(ResultSet resultSet) throws SQLException {

        Order order = new Order();

        order.setOrderId(resultSet.getInt("order_id"));
        order.setCustomerId(resultSet.getInt("customer_id"));
        order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        order.setOrderDate(resultSet.getTimestamp("order_date"));

        return order;
    }

    @Override
    public int save(Order order, Connection connection) throws SQLException {

        String sql = "INSERT INTO orders (customer_id, total_amount, order_date) " +
                     "VALUES (?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getCustomerId());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setTimestamp(3, order.getOrderDate());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        throw new SQLException("Failed to retrieve generated order ID");
    }
}