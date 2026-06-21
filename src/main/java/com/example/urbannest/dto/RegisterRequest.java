package com.example.urbannest.dto;

import com.example.urbannest.model.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private UserRole role; // Will parse "ROLE_TENANT" or "ROLE_OWNER"
}
