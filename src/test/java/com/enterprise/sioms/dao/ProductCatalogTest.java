package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCatalogTest {

    private final ProductDao productDao =
            new ProductDaoImpl();

    @Test
    void shouldFindAvailableProducts() {

        String productName =
                "Catalog Available Product " + System.currentTimeMillis();

        Product product = new Product(
                productName,
                "Test Category",
                new BigDecimal("100.00"),
                10,
                new Timestamp(System.currentTimeMillis())
        );

        productDao.save(product);

        List<Product> products =
                productDao.findAvailableProducts();

        assertTrue(products.stream()
                .anyMatch(p ->
                        p.getProductName().equals(productName)
                ));
    }

    @Test
    void shouldNotFindOutOfStockProducts() {

        String productName =
                "Out Of Stock Product " + System.currentTimeMillis();

        Product product = new Product(
                productName,
                "Test Category",
                new BigDecimal("100.00"),
                1,
                new Timestamp(System.currentTimeMillis())
        );

        productDao.save(product);

        Product savedProduct = productDao.findAll()
                .stream()
                .filter(p ->
                        p.getProductName().equals(productName)
                )
                .findFirst()
                .orElseThrow();

        savedProduct.setStockQuantity(0);

        productDao.update(savedProduct);

        List<Product> products =
                productDao.findAvailableProducts();

        assertFalse(products.stream()
                .anyMatch(p ->
                        p.getProductName().equals(productName)
                ));
    }

    @Test
    void shouldFindProductsByCategory() {

        String productName =
                "Laptop Catalog Product " + System.currentTimeMillis();

        Product product = new Product(
                productName,
                "Electronics",
                new BigDecimal("50000.00"),
                5,
                new Timestamp(System.currentTimeMillis())
        );

        productDao.save(product);

        List<Product> products =
                productDao.findByCategory("Electronics");

        assertTrue(products.stream()
                .anyMatch(p ->
                        p.getProductName().equals(productName)
                ));
    }

    @Test
    void shouldNotReturnOutOfStockProductsWhenFilteringByCategory() {

        String productName =
                "Out Of Stock Electronics " + System.currentTimeMillis();

        Product product = new Product(
                productName,
                "Electronics",
                new BigDecimal("20000.00"),
                1,
                new Timestamp(System.currentTimeMillis())
        );

        productDao.save(product);

        Product savedProduct = productDao.findAll()
                .stream()
                .filter(p ->
                        p.getProductName().equals(productName)
                )
                .findFirst()
                .orElseThrow();

        savedProduct.setStockQuantity(0);

        productDao.update(savedProduct);

        List<Product> products =
                productDao.findByCategory("Electronics");

        assertFalse(products.stream()
                .anyMatch(p ->
                        p.getProductName().equals(productName)
                ));
    }
}