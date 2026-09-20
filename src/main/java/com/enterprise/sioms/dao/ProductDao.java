package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.Product;

import java.util.List;

public interface ProductDao extends BaseDao<Product> {

    List<Product> findAvailableProducts();

    List<Product> findByCategory(String category);
}