package com.enterprise.sioms.model;

import java.math.BigDecimal;

public class CartItem {

    private int productId;
    private int quantity;
    private BigDecimal price;

    public CartItem() {
    }

    public CartItem(int productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CartItem)) {
            return false;
        }

        CartItem cartItem = (CartItem) o;
        return productId == cartItem.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}