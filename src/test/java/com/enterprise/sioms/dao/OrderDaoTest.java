package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoTest {

    private final OrderDao orderDao = new OrderDaoImpl();

    @Test
    void testSaveAndFindById() {

        Order order = new Order(
                1,
                new BigDecimal("2500.00"),
                new Timestamp(System.currentTimeMillis())
        );

        orderDao.save(order);

        List<Order> orders = orderDao.findAll();

        Order savedOrder = orders.stream()
                .filter(o -> o.getCustomerId() == 1
                        && new BigDecimal("2500.00")
                        .compareTo(o.getTotalAmount()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        Order foundOrder =
                orderDao.findById(savedOrder.getOrderId());

        assertNotNull(foundOrder);

        assertEquals(
                savedOrder.getOrderId(),
                foundOrder.getOrderId()
        );

        assertEquals(
                savedOrder.getCustomerId(),
                foundOrder.getCustomerId()
        );

        assertEquals(
                0,
                savedOrder.getTotalAmount()
                        .compareTo(foundOrder.getTotalAmount())
        );

        orderDao.delete(savedOrder.getOrderId());
    }

    @Test
    void testFindAll() {

        List<Order> orders = orderDao.findAll();

        assertNotNull(orders);
    }

    @Test
    void testUpdate() {

        Order order = new Order(
                1,
                new BigDecimal("1000.00"),
                new Timestamp(System.currentTimeMillis())
        );

        orderDao.save(order);

        List<Order> orders = orderDao.findAll();

        Order savedOrder = orders.stream()
                .filter(o -> o.getCustomerId() == 1
                        && new BigDecimal("1000.00")
                        .compareTo(o.getTotalAmount()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        savedOrder.setTotalAmount(
                new BigDecimal("1500.00")
        );

        orderDao.update(savedOrder);

        Order updatedOrder =
                orderDao.findById(savedOrder.getOrderId());

        assertNotNull(updatedOrder);

        assertEquals(
                0,
                new BigDecimal("1500.00")
                        .compareTo(updatedOrder.getTotalAmount())
        );

        orderDao.delete(updatedOrder.getOrderId());
    }

    @Test
    void testDelete() {

        Order order = new Order(
                1,
                new BigDecimal("750.00"),
                new Timestamp(System.currentTimeMillis())
        );

        orderDao.save(order);

        List<Order> orders = orderDao.findAll();

        Order savedOrder = orders.stream()
                .filter(o -> o.getCustomerId() == 1
                        && new BigDecimal("750.00")
                        .compareTo(o.getTotalAmount()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        int orderId = savedOrder.getOrderId();

        orderDao.delete(orderId);

        Order deletedOrder =
                orderDao.findById(orderId);

        assertNull(deletedOrder);
    }

    @Test
    void testTransactionalSave() throws Exception {

        Order order = new Order(
                1,
                new BigDecimal("3000.00"),
                new Timestamp(System.currentTimeMillis())
        );

        int orderId;

        try (Connection connection =
                     com.enterprise.sioms.util.DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            orderId = orderDao.save(order, connection);

            connection.commit();
        }

        assertTrue(orderId > 0);

        Order savedOrder =
                orderDao.findById(orderId);

        assertNotNull(savedOrder);

        assertEquals(
                1,
                savedOrder.getCustomerId()
        );

        assertEquals(
                0,
                new BigDecimal("3000.00")
                        .compareTo(savedOrder.getTotalAmount())
        );

        orderDao.delete(orderId);
    }
}