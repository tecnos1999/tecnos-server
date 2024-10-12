package com.example.tecnosserver.user.dto;

import com.example.tecnosserver.system.security.UserRole;

public record LoginResponse(String token, String firstName, String lastName, String phoneNumber, String email , String profileUrl, UserRole userRole) {
}
