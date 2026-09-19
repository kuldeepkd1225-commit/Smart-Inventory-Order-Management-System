package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.ProductDao;
import com.enterprise.sioms.dao.ProductDaoImpl;
import com.enterprise.sioms.exception.OutOfStockException;
import com.enterprise.sioms.exception.ResourceNotFoundException;
import com.enterprise.sioms.model.Product;

public class InventoryService {

    private final ProductDao productDao;

    public InventoryService() {
        this.productDao = new ProductDaoImpl();
    }

    public Product getProductById(int productId)
            throws ResourceNotFoundException {

        Product product = productDao.findById(productId);

        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        return product;
    }

    public void reduceStock(int productId, int quantity)
            throws OutOfStockException, ResourceNotFoundException {

        Product product = getProductById(productId);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (quantity > product.getStockQuantity()) {
            throw new OutOfStockException("Insufficient stock");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);

        productDao.update(product);
    }
}