package com.enterprise.sioms.model;

import java.sql.Timestamp;

public class Customer extends User {

    public Customer(String name, String email, String password,
                    Timestamp createdAt) {

        super(name, email, password, "CUSTOMER", createdAt);
    }
}