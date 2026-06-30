package com.example.urbannest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String name;
    private String phoneNumber;

    public void setName(String name) { this.name = name; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
