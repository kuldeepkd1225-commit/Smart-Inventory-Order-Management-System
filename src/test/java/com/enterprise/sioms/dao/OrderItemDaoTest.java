package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Order;
import com.enterprise.sioms.model.OrderItem;
import com.enterprise.sioms.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemDaoTest {

    private final OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private final OrderDao orderDao = new OrderDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();

    private int getExistingOrderId() {

        List<Order> orders = orderDao.findAll();

        assertFalse(
                orders.isEmpty(),
                "At least one order must exist for this test."
        );

        return orders.get(0).getOrderId();
    }

    private int getExistingProductId() {

        List<Product> products = productDao.findAll();

        assertFalse(
                products.isEmpty(),
                "At least one product must exist for this test."
        );

        return products.get(0).getProductId();
    }

    @Test
    void testSaveAndFindById() {

        int orderId = getExistingOrderId();
        int productId = getExistingProductId();

        OrderItem orderItem = new OrderItem(
                orderId,
                productId,
                2,
                new BigDecimal("1000.00")
        );

        orderItemDao.save(orderItem);

        List<OrderItem> orderItems =
                orderItemDao.findAll();

        OrderItem savedOrderItem = orderItems.stream()
                .filter(item -> item.getOrderId() == orderId
                        && item.getProductId() == productId
                        && item.getQuantity() == 2
                        && new BigDecimal("1000.00")
                        .compareTo(item.getUnitPrice()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrderItem);

        OrderItem foundOrderItem =
                orderItemDao.findById(
                        savedOrderItem.getOrderItemId()
                );

        assertNotNull(foundOrderItem);

        assertEquals(
                savedOrderItem.getOrderItemId(),
                foundOrderItem.getOrderItemId()
        );

        assertEquals(
                savedOrderItem.getOrderId(),
                foundOrderItem.getOrderId()
        );

        assertEquals(
                savedOrderItem.getProductId(),
                foundOrderItem.getProductId()
        );

        assertEquals(
                savedOrderItem.getQuantity(),
                foundOrderItem.getQuantity()
        );

        assertEquals(
                0,
                savedOrderItem.getUnitPrice()
                        .compareTo(foundOrderItem.getUnitPrice())
        );

        orderItemDao.delete(
                savedOrderItem.getOrderItemId()
        );
    }

    @Test
    void testFindAll() {

        List<OrderItem> orderItems =
                orderItemDao.findAll();

        assertNotNull(orderItems);
    }

    @Test
    void testUpdate() {

        int orderId = getExistingOrderId();
        int productId = getExistingProductId();

        OrderItem orderItem = new OrderItem(
                orderId,
                productId,
                1,
                new BigDecimal("500.00")
        );

        orderItemDao.save(orderItem);

        List<OrderItem> orderItems =
                orderItemDao.findAll();

        OrderItem savedOrderItem = orderItems.stream()
                .filter(item -> item.getOrderId() == orderId
                        && item.getProductId() == productId
                        && item.getQuantity() == 1
                        && new BigDecimal("500.00")
                        .compareTo(item.getUnitPrice()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrderItem);

        savedOrderItem.setQuantity(3);
        savedOrderItem.setUnitPrice(
                new BigDecimal("600.00")
        );

        orderItemDao.update(savedOrderItem);

        OrderItem updatedOrderItem =
                orderItemDao.findById(
                        savedOrderItem.getOrderItemId()
                );

        assertNotNull(updatedOrderItem);

        assertEquals(
                3,
                updatedOrderItem.getQuantity()
        );

        assertEquals(
                0,
                new BigDecimal("600.00")
                        .compareTo(
                                updatedOrderItem.getUnitPrice()
                        )
        );

        orderItemDao.delete(
                updatedOrderItem.getOrderItemId()
        );
    }

    @Test
    void testDelete() {

        int orderId = getExistingOrderId();
        int productId = getExistingProductId();

        OrderItem orderItem = new OrderItem(
                orderId,
                productId,
                1,
                new BigDecimal("750.00")
        );

        orderItemDao.save(orderItem);

        List<OrderItem> orderItems =
                orderItemDao.findAll();

        OrderItem savedOrderItem = orderItems.stream()
                .filter(item -> item.getOrderId() == orderId
                        && item.getProductId() == productId
                        && item.getQuantity() == 1
                        && new BigDecimal("750.00")
                        .compareTo(item.getUnitPrice()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrderItem);

        int orderItemId =
                savedOrderItem.getOrderItemId();

        orderItemDao.delete(orderItemId);

        OrderItem deletedOrderItem =
                orderItemDao.findById(orderItemId);

        assertNull(deletedOrderItem);
    }

    @Test
    void testTransactionalSave() throws Exception {

        int orderId = getExistingOrderId();
        int productId = getExistingProductId();

        OrderItem orderItem = new OrderItem(
                orderId,
                productId,
                4,
                new BigDecimal("1000.00")
        );

        int orderItemId;

        try (Connection connection =
                     com.enterprise.sioms.util.DatabaseConnection
                             .getConnection()) {

            connection.setAutoCommit(false);

            orderItemDao.save(
                    orderItem,
                    connection
            );

            connection.commit();
        }

        List<OrderItem> orderItems =
                orderItemDao.findAll();

        OrderItem savedOrderItem = orderItems.stream()
                .filter(item -> item.getOrderId() == orderId
                        && item.getProductId() == productId
                        && item.getQuantity() == 4
                        && new BigDecimal("1000.00")
                        .compareTo(item.getUnitPrice()) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrderItem);

        orderItemId =
                savedOrderItem.getOrderItemId();

        OrderItem foundOrderItem =
                orderItemDao.findById(orderItemId);

        assertNotNull(foundOrderItem);

        assertEquals(
                4,
                foundOrderItem.getQuantity()
        );

        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(
                                foundOrderItem.getUnitPrice()
                        )
        );

        orderItemDao.delete(orderItemId);
    }
}