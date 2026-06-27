package com.example.urbannest.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomRequest {
    private String title;
    private String description;
    private Double rent;
    private Double deposit;
    private String address;
    private String city;
    private String locality;
    private String roomType;
    private Long ownerId; // Connects the listing to the landlord profile
    private List<Long> amenityIds;
}