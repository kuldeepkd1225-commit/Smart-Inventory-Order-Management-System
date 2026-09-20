package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {

    private final ProductDao productDao = new ProductDaoImpl();

    @Test
    void testSaveAndFindById() {

        Product product = new Product(
                "Test Product",
                "Test Category",
                new BigDecimal("999.99"),
                50,
                null
        );

        productDao.save(product);

        List<Product> products = productDao.findAll();

        Product savedProduct = products.stream()
                .filter(p -> "Test Product".equals(p.getProductName()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedProduct);
        assertEquals(
                "Test Product",
                savedProduct.getProductName()
        );
        assertEquals(
                "Test Category",
                savedProduct.getCategory()
        );
        assertEquals(
                new BigDecimal("999.99"),
                savedProduct.getPrice()
        );
        assertEquals(
                50,
                savedProduct.getStockQuantity()
        );

        Product foundProduct =
                productDao.findById(savedProduct.getProductId());

        assertNotNull(foundProduct);
        assertEquals(
                savedProduct.getProductId(),
                foundProduct.getProductId()
        );

        productDao.delete(savedProduct.getProductId());
    }

    @Test
    void testFindAll() {

        List<Product> products = productDao.findAll();

        assertNotNull(products);
    }

    @Test
    void testUpdate() {

        Product product = new Product(
                "Update Product",
                "Electronics",
                new BigDecimal("1500.00"),
                20,
                null
        );

        productDao.save(product);

        List<Product> products = productDao.findAll();

        Product savedProduct = products.stream()
                .filter(p -> "Update Product".equals(p.getProductName()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedProduct);

        savedProduct.setProductName("Updated Product");
        savedProduct.setPrice(new BigDecimal("1750.00"));
        savedProduct.setStockQuantity(30);

        productDao.update(savedProduct);

        Product updatedProduct =
                productDao.findById(savedProduct.getProductId());

        assertNotNull(updatedProduct);
        assertEquals(
                "Updated Product",
                updatedProduct.getProductName()
        );
        assertEquals(
                new BigDecimal("1750.00"),
                updatedProduct.getPrice()
        );
        assertEquals(
                30,
                updatedProduct.getStockQuantity()
        );

        productDao.delete(updatedProduct.getProductId());
    }

    @Test
    void testDelete() {

        Product product = new Product(
                "Delete Product",
                "Test Category",
                new BigDecimal("500.00"),
                10,
                null
        );

        productDao.save(product);

        List<Product> products = productDao.findAll();

        Product savedProduct = products.stream()
                .filter(p -> "Delete Product".equals(p.getProductName()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedProduct);

        int productId = savedProduct.getProductId();

        productDao.delete(productId);

        Product deletedProduct =
                productDao.findById(productId);

        assertNull(deletedProduct);
    }
}