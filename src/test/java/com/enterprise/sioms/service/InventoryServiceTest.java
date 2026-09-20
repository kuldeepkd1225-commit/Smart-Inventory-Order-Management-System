package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.ProductDao;
import com.enterprise.sioms.dao.ProductDaoImpl;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    private final ProductDao productDao =
            new ProductDaoImpl();

    private final InventoryService inventoryService =
            new InventoryService();

    @Test
    void testAddProduct()
            throws Exception {

        Product product = new Product(
                "Admin Add Test",
                "Test Category",
                new BigDecimal("1200.00"),
                25,
                null
        );

        inventoryService.addProduct(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Admin Add Test"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        assertEquals(
                "Test Category",
                savedProduct.getCategory()
        );

        assertEquals(
                0,
                new BigDecimal("1200.00")
                        .compareTo(savedProduct.getPrice())
        );

        assertEquals(
                25,
                savedProduct.getStockQuantity()
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testAddNullProduct() {

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(null)
        );
    }

    @Test
    void testAddProductWithInvalidName() {

        Product product = new Product(
                "",
                "Test Category",
                new BigDecimal("1000.00"),
                10,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(product)
        );
    }

    @Test
    void testAddProductWithInvalidCategory() {

        Product product = new Product(
                "Invalid Category Product",
                "",
                new BigDecimal("1000.00"),
                10,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(product)
        );
    }

    @Test
    void testAddProductWithInvalidPrice() {

        Product zeroPriceProduct = new Product(
                "Zero Price Product",
                "Test Category",
                BigDecimal.ZERO,
                10,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(
                        zeroPriceProduct
                )
        );

        Product negativePriceProduct = new Product(
                "Negative Price Product",
                "Test Category",
                new BigDecimal("-100.00"),
                10,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(
                        negativePriceProduct
                )
        );
    }

    @Test
    void testAddProductWithInvalidStock() {

        Product zeroStockProduct = new Product(
                "Zero Stock Product",
                "Test Category",
                new BigDecimal("1000.00"),
                0,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(
                        zeroStockProduct
                )
        );

        Product negativeStockProduct = new Product(
                "Negative Stock Product",
                "Test Category",
                new BigDecimal("1000.00"),
                -5,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.addProduct(
                        negativeStockProduct
                )
        );
    }

    @Test
    void testGetProductById()
            throws ResourceNotFoundException {

        Product product = new Product(
                "Inventory Test Product",
                "Test Category",
                new BigDecimal("1000.00"),
                50,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Inventory Test Product"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        Product foundProduct =
                inventoryService.getProductById(
                        savedProduct.getProductId()
                );

        assertNotNull(foundProduct);

        assertEquals(
                savedProduct.getProductId(),
                foundProduct.getProductId()
        );

        assertEquals(
                "Inventory Test Product",
                foundProduct.getProductName()
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testGetProductByIdNotFound() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.getProductById(999999)
        );
    }

    @Test
    void testUpdateProduct()
            throws Exception {

        Product product = new Product(
                "Update Test Product",
                "Old Category",
                new BigDecimal("1000.00"),
                20,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Update Test Product"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        savedProduct.setProductName(
                "Updated Product"
        );

        savedProduct.setCategory(
                "Updated Category"
        );

        savedProduct.setPrice(
                new BigDecimal("1500.00")
        );

        savedProduct.setStockQuantity(40);

        inventoryService.updateProduct(
                savedProduct
        );

        Product updatedProduct =
                productDao.findById(
                        savedProduct.getProductId()
                );

        assertNotNull(updatedProduct);

        assertEquals(
                "Updated Product",
                updatedProduct.getProductName()
        );

        assertEquals(
                "Updated Category",
                updatedProduct.getCategory()
        );

        assertEquals(
                0,
                new BigDecimal("1500.00")
                        .compareTo(
                                updatedProduct.getPrice()
                        )
        );

        assertEquals(
                40,
                updatedProduct.getStockQuantity()
        );

        productDao.delete(
                updatedProduct.getProductId()
        );
    }

    @Test
    void testUpdateNullProduct() {

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.updateProduct(null)
        );
    }

    @Test
    void testUpdateProductNotFound() {

        Product product = new Product(
                "Missing Product",
                "Test Category",
                new BigDecimal("1000.00"),
                10,
                null
        );

        product.setProductId(999999);

        assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.updateProduct(product)
        );
    }

    @Test
    void testUpdateProductWithInvalidPrice()
            throws Exception {

        Product product = new Product(
                "Invalid Update Price",
                "Test Category",
                new BigDecimal("1000.00"),
                10,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Invalid Update Price"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        savedProduct.setPrice(
                BigDecimal.ZERO
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.updateProduct(
                        savedProduct
                )
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testUpdateProductWithInvalidStock()
            throws Exception {

        Product product = new Product(
                "Invalid Update Stock",
                "Test Category",
                new BigDecimal("1000.00"),
                10,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Invalid Update Stock"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        savedProduct.setStockQuantity(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.updateProduct(
                        savedProduct
                )
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testReduceStock()
            throws Exception {

        Product product = new Product(
                "Stock Reduction Test",
                "Test Category",
                new BigDecimal("500.00"),
                20,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Stock Reduction Test"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        int initialStock =
                savedProduct.getStockQuantity();

        inventoryService.reduceStock(
                savedProduct.getProductId(),
                5
        );

        Product updatedProduct =
                productDao.findById(
                        savedProduct.getProductId()
                );

        assertNotNull(updatedProduct);

        assertEquals(
                initialStock - 5,
                updatedProduct.getStockQuantity()
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testReduceStockWhenInsufficient()
            throws Exception {

        Product product = new Product(
                "Out Of Stock Test",
                "Test Category",
                new BigDecimal("750.00"),
                5,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Out Of Stock Test"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        assertThrows(
                OutOfStockException.class,
                () -> inventoryService.reduceStock(
                        savedProduct.getProductId(),
                        10
                )
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }

    @Test
    void testReduceStockWithInvalidQuantity()
            throws Exception {

        Product product = new Product(
                "Invalid Quantity Test",
                "Test Category",
                new BigDecimal("900.00"),
                20,
                null
        );

        productDao.save(product);

        Product savedProduct =
                productDao.findAll()
                        .stream()
                        .filter(p ->
                                "Invalid Quantity Test"
                                        .equals(p.getProductName()))
                        .findFirst()
                        .orElse(null);

        assertNotNull(savedProduct);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(
                        savedProduct.getProductId(),
                        0
                )
        );

        productDao.delete(
                savedProduct.getProductId()
        );
    }
}