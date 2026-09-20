package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.User;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private final UserDao userDao = new UserDaoImpl();

    @Test
    void testSaveAndFindById() {

        User user = new User(
                "Test User",
                "testuser@sioms.com",
                "test123",
                "CUSTOMER",
                new Timestamp(System.currentTimeMillis())
        );

        userDao.save(user);

        User savedUser =
                userDao.findByEmail("testuser@sioms.com");

        assertNotNull(savedUser);
        assertEquals("Test User", savedUser.getName());
        assertEquals(
                "testuser@sioms.com",
                savedUser.getEmail()
        );

        User foundUser =
                userDao.findById(savedUser.getUserId());

        assertNotNull(foundUser);
        assertEquals(
                savedUser.getUserId(),
                foundUser.getUserId()
        );

        userDao.delete(savedUser.getUserId());
    }

    @Test
    void testFindAll() {

        List<User> users = userDao.findAll();

        assertNotNull(users);
    }

    @Test
    void testUpdate() {

        User user = new User(
                "Update User",
                "updateuser@sioms.com",
                "password123",
                "CUSTOMER",
                new Timestamp(System.currentTimeMillis())
        );

        userDao.save(user);

        User savedUser =
                userDao.findByEmail("updateuser@sioms.com");

        assertNotNull(savedUser);

        savedUser.setName("Updated Name");

        userDao.update(savedUser);

        User updatedUser =
                userDao.findById(savedUser.getUserId());

        assertNotNull(updatedUser);
        assertEquals(
                "Updated Name",
                updatedUser.getName()
        );

        userDao.delete(updatedUser.getUserId());
    }

    @Test
    void testDelete() {

        User user = new User(
                "Delete User",
                "deleteuser@sioms.com",
                "password123",
                "CUSTOMER",
                new Timestamp(System.currentTimeMillis())
        );

        userDao.save(user);

        User savedUser =
                userDao.findByEmail("deleteuser@sioms.com");

        assertNotNull(savedUser);

        int userId = savedUser.getUserId();

        userDao.delete(userId);

        User deletedUser =
                userDao.findById(userId);

        assertNull(deletedUser);
    }

    @Test
    void testFindByEmail() {

        User user = new User(
                "Email Test User",
                "emailtest@sioms.com",
                "password123",
                "CUSTOMER",
                new Timestamp(System.currentTimeMillis())
        );

        userDao.save(user);

        User foundUser =
                userDao.findByEmail("emailtest@sioms.com");

        assertNotNull(foundUser);
        assertEquals(
                "Email Test User",
                foundUser.getName()
        );
        assertEquals(
                "emailtest@sioms.com",
                foundUser.getEmail()
        );

        userDao.delete(foundUser.getUserId());
    }
}