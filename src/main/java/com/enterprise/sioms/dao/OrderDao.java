package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDao extends BaseDao<Order> {

    int save(Order order, Connection connection) throws SQLException;
}