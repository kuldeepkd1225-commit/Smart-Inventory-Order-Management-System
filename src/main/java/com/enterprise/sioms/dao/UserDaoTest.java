package com.enterprise.sioms.dao;

import com.enterprise.sioms.model.User;

public class UserDaoTest {

    public static void main(String[] args) {

        UserDao userDao = new UserDaoImpl();

        // ------------------------------------------------
        // 1. SAVE - Add a new user
        // ------------------------------------------------

        User user = new User(
                "Test User",
                "testuser3@gmail.com",
                "test123",
                "CUSTOMER",
                null
        );

        userDao.save(user);

        System.out.println("\nUser saved.");


        // ------------------------------------------------
        // 2. FIND ALL - Display all users
        // ------------------------------------------------

        System.out.println("\nAll Users:");

        for (User u : userDao.findAll()) {
            System.out.println(u);
        }


        // ------------------------------------------------
        // 3. FIND BY ID - Find user with ID 2
        // ------------------------------------------------

        System.out.println("\nFinding user with ID 2:");

        User foundUser = userDao.findById(2);

        if (foundUser != null) {
            System.out.println(foundUser);
        } else {
            System.out.println("User not found.");
        }


        // ------------------------------------------------
        // 4. UPDATE - Update user with ID 2
        // ------------------------------------------------

        User userToUpdate = userDao.findById(2);

        if (userToUpdate != null) {

            userToUpdate.setName("Updated User");
            userToUpdate.setEmail("updateduser@gmail.com");

            userDao.update(userToUpdate);

            System.out.println("\nAfter update:");

            System.out.println(userDao.findById(2));

        } else {
            System.out.println("\nUser with ID 2 not found.");
        }


        // ------------------------------------------------
        // 5. DELETE - Delete user with ID 2
        // ------------------------------------------------

        userDao.delete(2);

        System.out.println("\nAfter delete:");

        User deletedUser = userDao.findById(2);

        if (deletedUser == null) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User was not deleted.");
        }
    }
}