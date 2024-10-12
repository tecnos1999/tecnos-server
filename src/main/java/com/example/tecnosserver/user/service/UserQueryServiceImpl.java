package com.example.tecnosserver.user.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.user.model.User;
import com.example.tecnosserver.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepo userRepo;


    public UserQueryServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user;
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }
    }
}
