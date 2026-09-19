package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderItemDao extends BaseDao<OrderItem> {

    void save(OrderItem orderItem, Connection connection) throws SQLException;
}