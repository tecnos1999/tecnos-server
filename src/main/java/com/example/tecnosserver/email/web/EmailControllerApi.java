package com.example.tecnosserver.email.web;

import com.example.tecnosserver.email.service.EmailServiceCommandImpl;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tecnosserver.email.model.Email;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/email")
@Slf4j
public class EmailControllerApi {
    private final EmailServiceCommandImpl emailServiceCommand;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    public EmailControllerApi(EmailServiceCommandImpl emailServiceCommand, AdvancedStructuredLogger advancedStructuredLogger) {
        this.emailServiceCommand = emailServiceCommand;
        this.advancedStructuredLogger = advancedStructuredLogger;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {

        advancedStructuredLogger.logBuilder()
                .withEventType("CONTROLLER_EMAIL_REQUEST")
                .withMessage("Received request to send an email")
                .withField("customerName", email.getFullName())
                .withField("customerEmail", email.getEmail())
                .withLevel("INFO")
                .log();

        emailServiceCommand.sendEmail(email);

        advancedStructuredLogger.logBuilder()
                .withEventType("CONTROLLER_EMAIL_SENT")
                .withMessage("Email request was processed successfully by controller")
                .withField("customerName", email.getFullName())
                .withLevel("INFO")
                .log();

        return ResponseEntity.ok("Email sent");
    }


}
