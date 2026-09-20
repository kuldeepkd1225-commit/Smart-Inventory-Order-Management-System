package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.OrderDao;
import com.enterprise.sioms.dao.OrderDaoImpl;
import com.enterprise.sioms.dao.ProductDao;
import com.enterprise.sioms.dao.ProductDaoImpl;
import com.enterprise.sioms.exception.InvalidUserException;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.CartItem;
import com.enterprise.sioms.model.Order;
import com.enterprise.sioms.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private final OrderService orderService =
            new OrderService();

    private final OrderDao orderDao =
            new OrderDaoImpl();

    private final ProductDao productDao =
            new ProductDaoImpl();

    private int getExistingCustomerId() {

        List<Order> orders =
                orderDao.findAll();

        assertFalse(
                orders.isEmpty(),
                "At least one order must exist for this test."
        );

        return orders.get(0).getCustomerId();
    }

    private Product getExistingProductWithStock(int requiredStock) {

        List<Product> products =
                productDao.findAll();

        Product product = products.stream()
                .filter(p ->
                        p.getStockQuantity() >= requiredStock)
                .findFirst()
                .orElse(null);

        assertNotNull(
                product,
                "A product with sufficient stock must exist."
        );

        return product;
    }

    @Test
    void testCreateOrder()
            throws Exception {

        int customerId =
                getExistingCustomerId();

        Order order = new Order(
                customerId,
                new BigDecimal("2500.00"),
                new Timestamp(
                        System.currentTimeMillis()
                )
        );

        orderService.createOrder(order);

        List<Order> orders =
                orderDao.findAll();

        Order savedOrder = orders.stream()
                .filter(o ->
                        o.getCustomerId() == customerId
                                && new BigDecimal("2500.00")
                                .compareTo(
                                        o.getTotalAmount()
                                ) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        orderDao.delete(
                savedOrder.getOrderId()
        );
    }

    @Test
    void testCreateOrderWithInvalidCustomer() {

        Order order = new Order(
                0,
                new BigDecimal("1000.00"),
                new Timestamp(
                        System.currentTimeMillis()
                )
        );

        assertThrows(
                InvalidUserException.class,
                () -> orderService.createOrder(order)
        );
    }

    @Test
    void testGetOrderById()
            throws Exception {

        int customerId =
                getExistingCustomerId();

        Order order = new Order(
                customerId,
                new BigDecimal("1800.00"),
                new Timestamp(
                        System.currentTimeMillis()
                )
        );

        orderDao.save(order);

        List<Order> orders =
                orderDao.findAll();

        Order savedOrder = orders.stream()
                .filter(o ->
                        o.getCustomerId() == customerId
                                && new BigDecimal("1800.00")
                                .compareTo(
                                        o.getTotalAmount()
                                ) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        Order foundOrder =
                orderService.getOrderById(
                        savedOrder.getOrderId()
                );

        assertNotNull(foundOrder);

        assertEquals(
                savedOrder.getOrderId(),
                foundOrder.getOrderId()
        );

        assertEquals(
                0,
                savedOrder.getTotalAmount()
                        .compareTo(
                                foundOrder.getTotalAmount()
                        )
        );

        orderDao.delete(
                savedOrder.getOrderId()
        );
    }

    @Test
    void testGetOrderByIdNotFound() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(999999)
        );
    }

    @Test
    void testCheckoutSuccessfully()
            throws Exception {

        int customerId =
                getExistingCustomerId();

        Product product =
                getExistingProductWithStock(2);

        int initialStock =
                product.getStockQuantity();

        int initialOrderCount =
                orderDao.findAll().size();

        BigDecimal unitPrice =
                product.getPrice();

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        product.getProductId(),
                        2,
                        unitPrice
                )
        );

        orderService.checkout(
                customerId,
                cartItems
        );

        List<Order> orders =
                orderDao.findAll();

        assertEquals(
                initialOrderCount + 1,
                orders.size()
        );

        BigDecimal expectedTotal =
                unitPrice.multiply(
                        new BigDecimal("2")
                );

        Order createdOrder = orders.stream()
                .filter(o ->
                        o.getCustomerId() == customerId
                                && expectedTotal.compareTo(
                                        o.getTotalAmount()
                                ) == 0)
                .findFirst()
                .orElse(null);

        assertNotNull(createdOrder);

        Product updatedProduct =
                productDao.findById(
                        product.getProductId()
                );

        assertNotNull(updatedProduct);

        assertEquals(
                initialStock - 2,
                updatedProduct.getStockQuantity()
        );

        orderDao.delete(
                createdOrder.getOrderId()
        );

        Product restoredProduct =
                productDao.findById(
                        product.getProductId()
                );

        assertNotNull(restoredProduct);

        restoredProduct.setStockQuantity(
                initialStock
        );

        productDao.update(restoredProduct);
    }

    @Test
    void testCheckoutWithInvalidCustomer() {

        Product product =
                getExistingProductWithStock(1);

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        product.getProductId(),
                        1,
                        product.getPrice()
                )
        );

        assertThrows(
                InvalidUserException.class,
                () -> orderService.checkout(
                        0,
                        cartItems
                )
        );
    }

    @Test
    void testCheckoutWithEmptyCart() {

        int customerId =
                getExistingCustomerId();

        List<CartItem> cartItems =
                new ArrayList<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.checkout(
                        customerId,
                        cartItems
                )
        );
    }

    @Test
    void testCheckoutWithInvalidQuantity() {

        int customerId =
                getExistingCustomerId();

        Product product =
                getExistingProductWithStock(1);

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        product.getProductId(),
                        0,
                        product.getPrice()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.checkout(
                        customerId,
                        cartItems
                )
        );
    }

    @Test
    void testCheckoutWithNullPrice() {

        int customerId =
                getExistingCustomerId();

        Product product =
                getExistingProductWithStock(1);

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        product.getProductId(),
                        1,
                        null
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.checkout(
                        customerId,
                        cartItems
                )
        );
    }

    @Test
    void testCheckoutWithInsufficientStock()
            throws Exception {

        int customerId =
                getExistingCustomerId();

        Product product =
                getExistingProductWithStock(1);

        int initialStock =
                product.getStockQuantity();

        int initialOrderCount =
                orderDao.findAll().size();

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        product.getProductId(),
                        initialStock + 1,
                        product.getPrice()
                )
        );

        assertThrows(
                OutOfStockException.class,
                () -> orderService.checkout(
                        customerId,
                        cartItems
                )
        );

        Product unchangedProduct =
                productDao.findById(
                        product.getProductId()
                );

        assertNotNull(unchangedProduct);

        assertEquals(
                initialStock,
                unchangedProduct.getStockQuantity()
        );

        assertEquals(
                initialOrderCount,
                orderDao.findAll().size()
        );
    }

    @Test
    void testCheckoutWithProductNotFound()
            throws Exception {

        int customerId =
                getExistingCustomerId();

        int initialOrderCount =
                orderDao.findAll().size();

        List<CartItem> cartItems =
                new ArrayList<>();

        cartItems.add(
                new CartItem(
                        999999,
                        1,
                        new BigDecimal("100.00")
                )
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.checkout(
                        customerId,
                        cartItems
                )
        );

        assertEquals(
                initialOrderCount,
                orderDao.findAll().size()
        );
    }
}