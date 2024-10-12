package com.example.tecnosserver.user.service;

import com.example.tecnosserver.user.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserCommandService {

    void addUser(UserDTO userDTO);

    void updateUser(String email , UserDTO userDTO);

    void deleteUser(String email);

    void updateProfileUrl(String email, String profileUrl);
}