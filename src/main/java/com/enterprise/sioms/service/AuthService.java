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

    public User register(
            String name,
            String email,
            String password,
            String role
    ) {

        if (name == null ||
                name.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name cannot be empty"
            );
        }

        if (email == null ||
                email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }

        if (password == null ||
                password.length() < 6) {

            throw new IllegalArgumentException(
                    "Password must be at least 6 characters"
            );
        }

        if (role == null ||
                !(role.equalsIgnoreCase("ADMIN") ||
                  role.equalsIgnoreCase("CUSTOMER"))) {

            throw new IllegalArgumentException(
                    "Role must be ADMIN or CUSTOMER"
            );
        }

        email = email.trim();
        role = role.toUpperCase();

        if (userDao.findByEmail(email) != null) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        User user = new User(
                name.trim(),
                email,
                password,
                role,
                null
        );

        userDao.save(user);

        return userDao.findByEmail(email);
    }

    public User login(
            String email,
            String password
    ) throws InvalidUserException {

        User user =
                userDao.findByEmail(email);

        if (user == null ||
                !user.getPassword().equals(password)) {

            throw new InvalidUserException(
                    "Invalid email or password"
            );
        }

        return user;
    }
}