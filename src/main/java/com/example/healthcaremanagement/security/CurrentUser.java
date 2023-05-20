package com.example.healthcaremanagement.security;


import com.example.healthcaremanagement.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User currentUser;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("USER"));
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
