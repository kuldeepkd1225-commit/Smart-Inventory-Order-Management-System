package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.ProductDao;
import com.enterprise.sioms.dao.ProductDaoImpl;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.Product;

import java.util.List;

public class InventoryService {

    private final ProductDao productDao;

    public InventoryService() {
        this.productDao = new ProductDaoImpl();
    }

    public void addProduct(Product product)
            throws ResourceNotFoundException {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null"
            );
        }

        if (product.getProductName() == null ||
                product.getProductName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Product name cannot be empty"
            );
        }

        if (product.getCategory() == null ||
                product.getCategory().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Product category cannot be empty"
            );
        }

        if (product.getPrice() == null ||
                product.getPrice().signum() <= 0) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }

        if (product.getStockQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Stock quantity must be greater than zero"
            );
        }

        productDao.save(product);
    }

    public Product getProductById(int productId)
            throws ResourceNotFoundException {

        Product product =
                productDao.findById(productId);

        if (product == null) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        return product;
    }

    public List<Product> getAvailableProducts() {

        return productDao.findAvailableProducts();
    }

    public List<Product> getProductsByCategory(
            String category) {

        if (category == null ||
                category.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Category cannot be empty"
            );
        }

        return productDao.findByCategory(
                category.trim()
        );
    }

    public void updateProduct(Product product)
            throws ResourceNotFoundException {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null"
            );
        }

        if (product.getProductId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid product ID"
            );
        }

        Product existingProduct =
                productDao.findById(
                        product.getProductId()
                );

        if (existingProduct == null) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        if (product.getPrice() == null ||
                product.getPrice().signum() <= 0) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }

        if (product.getStockQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Stock quantity must be greater than zero"
            );
        }

        productDao.update(product);
    }

    public void reduceStock(
            int productId,
            int quantity
    ) throws OutOfStockException,
            ResourceNotFoundException {

        Product product =
                getProductById(productId);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        if (quantity > product.getStockQuantity()) {
            throw new OutOfStockException(
                    "Insufficient stock"
            );
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );

        productDao.update(product);
    }
}