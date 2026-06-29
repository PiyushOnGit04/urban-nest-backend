package com.example.urbannest.repository;

import com.example.urbannest.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>,
        JpaSpecificationExecutor<Room> {

    List<Room> findByAvailableTrue();

    List<Room> findAll();

    List<Room> findByCity(String city);

    List<Room> findByCityAndAvailableTrue(String city);

    List<Room> findByOwnerId(Long ownerId);

    List<Room> findByCityContainingIgnoreCaseAndAvailableTrue(String city);

    List<Room> findByLocalityContainingIgnoreCaseAndAvailableTrue(String locality);

    List<Room> findByRentBetweenAndAvailableTrue(Double minRent, Double maxRent);
}