package com.enterprise.sioms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {

    private int productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private int stockQuantity;
    private Timestamp updatedAt;

    public Product() {
    }

    public Product(String productName, String category,BigDecimal price, int stockQuantity, Timestamp updatedAt) {

        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.updatedAt = updatedAt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Product)) {
            return false;
        }

        Product product = (Product) o;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}