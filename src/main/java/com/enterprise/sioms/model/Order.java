package com.enterprise.sioms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private int customerId;
    private BigDecimal totalAmount;
    private Timestamp orderDate;

    public Order() {
    }

    public Order(int customerId, BigDecimal totalAmount, Timestamp orderDate) {

        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}