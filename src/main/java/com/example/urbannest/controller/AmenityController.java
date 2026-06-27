package com.example.urbannest.controller;

import com.example.urbannest.model.Amenity;
import com.example.urbannest.service.AmenityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@CrossOrigin(origins = "*")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }
}
