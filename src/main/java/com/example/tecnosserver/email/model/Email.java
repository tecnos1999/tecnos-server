package com.example.tecnosserver.email.model;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Email {
    private String fullName;
    private String company;
    private String email;
    private String phone;
    private String judet;
    private String message;
    private List<String> products;
}
