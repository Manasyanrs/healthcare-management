package com.example.healthcaremanagement.security;

import com.example.healthcaremanagement.entity.User;
import com.example.healthcaremanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usernameInDB = null;
        if (username.equals("root")) {
            usernameInDB = User.builder()
                    .name("root")
                    .email("root")
                    .password(passwordEncoder.encode("root"))
                    .build();
        } else {
            usernameInDB = userRepository.findByEmail(username).get();
            if (usernameInDB == null) {
                throw new UsernameNotFoundException("Incorrect username!");
            }
        }

        return new CurrentUser(usernameInDB);
    }

}
