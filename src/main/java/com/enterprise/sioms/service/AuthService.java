package com.enterprise.sioms.service;

import com.enterprise.sioms.dao.UserDao;
import com.enterprise.sioms.dao.UserDaoImpl;
import com.enterprise.sioms.exception.InvalidUserException;
import com.enterprise.sioms.model.User;

public class AuthService {

    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDaoImpl();
    }

    public User login(String email, String password) throws InvalidUserException {

        User user = userDao.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidUserException("Invalid email or password");
        }

        return user;
    }
}