package com.enterprise.sioms.service;

import com.enterprise.sioms.exception.InvalidUserException;
import com.enterprise.sioms.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private final AuthService authService =
            new AuthService();

    @Test
    void shouldRegisterUser() {

        String email =
                "register_" +
                System.currentTimeMillis() +
                "@test.com";

        User user = authService.register(
                "Registration Test",
                email,
                "password123",
                "CUSTOMER"
        );

        assertNotNull(user);

        assertEquals(
                "Registration Test",
                user.getName()
        );

        assertEquals(
                email,
                user.getEmail()
        );

        assertEquals(
                "CUSTOMER",
                user.getRole()
        );
    }

    @Test
    void shouldRejectShortPassword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(
                        "Test User",
                        "shortpassword@test.com",
                        "12345",
                        "CUSTOMER"
                )
        );
    }

    @Test
    void shouldRejectInvalidRole() {

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(
                        "Test User",
                        "invalidrole@test.com",
                        "password123",
                        "MANAGER"
                )
        );
    }

    @Test
    void shouldRejectEmptyName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(
                        "",
                        "emptyname@test.com",
                        "password123",
                        "CUSTOMER"
                )
        );
    }

    @Test
    void shouldRejectEmptyEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(
                        "Test User",
                        "",
                        "password123",
                        "CUSTOMER"
                )
        );
    }

    @Test
    void shouldRejectDuplicateEmail() {

        String email =
                "duplicate_" +
                System.currentTimeMillis() +
                "@test.com";

        authService.register(
                "First User",
                email,
                "password123",
                "CUSTOMER"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(
                        "Second User",
                        email,
                        "password456",
                        "CUSTOMER"
                )
        );
    }

    @Test
    void shouldLoginRegisteredUser()
            throws InvalidUserException {

        String email =
                "login_" +
                System.currentTimeMillis() +
                "@test.com";

        authService.register(
                "Login Test",
                email,
                "password123",
                "CUSTOMER"
        );

        User user =
                authService.login(
                        email,
                        "password123"
                );

        assertNotNull(user);

        assertEquals(
                email,
                user.getEmail()
        );
    }

    @Test
    void shouldRejectInvalidLogin() {

        assertThrows(
                InvalidUserException.class,
                () -> authService.login(
                        "doesnotexist@test.com",
                        "wrongpassword"
                )
        );
    }
}