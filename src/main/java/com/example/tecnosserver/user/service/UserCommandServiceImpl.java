package com.example.tecnosserver.user.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.system.security.UserRole;
import com.example.tecnosserver.user.dto.UserDTO;
import com.example.tecnosserver.user.model.User;
import com.example.tecnosserver.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void addUser(UserDTO userDTO) {

        Optional<User> user = userRepo.findByEmail(userDTO.email());
        if (user.isEmpty()) {
            User x = User.builder()
                    .email(userDTO.email())
                    .firstName(userDTO.firstName())
                    .lastName(userDTO.lastName())
                    .phoneNumber(userDTO.phoneNumber())
                    .password(passwordEncoder.encode(userDTO.password()))
                    .registeredAt(LocalDateTime.now())
                    .createdAt(userDTO.createdAt())
                    .userRole(UserRole.CLIENT)
                    .build();
            userRepo.saveAndFlush(x);
        } else {
            throw new AlreadyExistsException("User with email " + userDTO.email() + " already exists");
        }
    }

    @Override
    public void updateUser(String email, UserDTO userDTO) {

        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            User x = user.get();
            x.setEmail(userDTO.email());
            x.setFirstName(userDTO.firstName());
            x.setLastName(userDTO.lastName());
            x.setPhoneNumber(userDTO.phoneNumber());
            x.setPassword(userDTO.password());
            userRepo.saveAndFlush(x);
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }

    }

    @Override
    public void deleteUser(String email) {

        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            userRepo.delete(user.get());
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }

    }


    @Override
    public void updateProfileUrl(String email, String profileUrl) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            User x = user.get();
            x.setProfileUrl(profileUrl);
            userRepo.saveAndFlush(x);
        } else {
            throw new NotFoundException("User with email " + email + " not found");
        }
    }
}
