package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.User;

public interface UserDao extends BaseDao<User> {

    User findByEmail(String email);
}