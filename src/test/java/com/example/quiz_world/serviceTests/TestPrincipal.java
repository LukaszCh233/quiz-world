package com.example.quiz_world.serviceTests;

import java.security.Principal;

public class TestPrincipal implements Principal {
    private final String email;

    public TestPrincipal(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}

