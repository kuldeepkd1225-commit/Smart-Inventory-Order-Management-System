package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Product;

import java.math.BigDecimal;

public class ProductDaoTest {

    public static void main(String[] args) {

        ProductDao productDao = new ProductDaoImpl();

        // ------------------------------------------------
        // 1. SAVE - Add a new product
        // ------------------------------------------------

        Product product = new Product(
                "Test Laptop",
                "Electronics",
                new BigDecimal("55000.00"),
                10,
                null
        );

        productDao.save(product);

        System.out.println("\nProduct saved.");


        // ------------------------------------------------
        // 2. FIND ALL - Display all products
        // ------------------------------------------------

        System.out.println("\nAll Products:");

        for (Product p : productDao.findAll()) {
            System.out.println(p);
        }


        // ------------------------------------------------
        // 3. FIND BY ID - Find product with ID 1
        // ------------------------------------------------

        System.out.println("\nFinding product with ID 1:");

        Product foundProduct = productDao.findById(1);

        if (foundProduct != null) {
            System.out.println(foundProduct);
        } else {
            System.out.println("Product not found.");
        }


        // ------------------------------------------------
        // 4. UPDATE - Update product with ID 1
        // ------------------------------------------------

        Product productToUpdate = productDao.findById(1);

        if (productToUpdate != null) {

            productToUpdate.setProductName("Updated Laptop");
            productToUpdate.setPrice(new BigDecimal("60000.00"));
            productToUpdate.setStockQuantity(15);

            productDao.update(productToUpdate);

            System.out.println("\nAfter update:");

            System.out.println(productDao.findById(1));

        } else {
            System.out.println("\nProduct with ID 1 not found.");
        }


        // ------------------------------------------------
        // 5. DELETE - Delete product with ID 1
        // ------------------------------------------------

        productDao.delete(1);

        System.out.println("\nAfter delete:");

        Product deletedProduct = productDao.findById(1);

        if (deletedProduct == null) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product was not deleted.");
        }
    }
}