package com.enterprise.sioms.model;

import java.sql.Timestamp;

public class Admin extends User {

    public Admin(String name, String email, String password,
                 Timestamp createdAt) {

        super(name, email, password, "ADMIN", createdAt);
    }
    
}