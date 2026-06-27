package com.example.urbannest.service;

import com.example.urbannest.model.Amenity;
import com.example.urbannest.repository.AmenityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public List<Amenity> getAmenitiesByIds(List<Long> ids) {
        return amenityRepository.findAllById(ids);
    }
}