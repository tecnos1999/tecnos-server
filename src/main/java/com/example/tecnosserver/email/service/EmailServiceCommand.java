package com.example.tecnosserver.email.service;


import com.example.tecnosserver.email.model.Email;

public interface EmailServiceCommand {
    void sendEmail(Email email);
}
