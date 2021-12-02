package com.example.demo.springSecurityDatabase;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    //Load user by JWT subject (ID) instead Username
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> user = repository.findById(Integer.valueOf(id));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Don't exists user with given token!");
        }
        return new CustomUserDetails(user.get());
    }
}
