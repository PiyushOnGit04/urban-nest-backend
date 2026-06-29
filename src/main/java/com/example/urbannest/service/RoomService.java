package com.example.urbannest.service;

import com.example.urbannest.dto.RoomRequest;
import com.example.urbannest.model.Amenity;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomType;
import com.example.urbannest.model.User;
import com.example.urbannest.repository.AmenityRepository;
import com.example.urbannest.repository.RoomRepository;
import com.example.urbannest.specification.RoomSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final AmenityRepository amenityRepository;

    public RoomService(RoomRepository roomRepository,
                       AmenityRepository amenityRepository) {
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }

    public Room createRoom(RoomRequest roomDto, User owner) {

        List<Amenity> amenities =
                amenityRepository.findAllById(roomDto.getAmenityIds());

        Room room = Room.builder()
                .title(roomDto.getTitle())
                .description(roomDto.getDescription())
                .rent(roomDto.getRent())
                .deposit(roomDto.getDeposit())
                .address(roomDto.getAddress())
                .city(roomDto.getCity())
                .locality(roomDto.getLocality())
                .roomType(RoomType.valueOf(roomDto.getRoomType()))
                .owner(owner)
                .available(true)
                .amenities(amenities)
                .build();

        return roomRepository.save(room);
    }

    public List<Room> getAllAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public List<Room> getRoomsByCity(String city) {
        return roomRepository.findByCityAndAvailableTrue(city);
    }

    public List<Room> getRoomsByOwner(Long ownerId) {
        return roomRepository.findByOwnerId(ownerId);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room updateRoomAvailability(Long roomId, boolean isAvailable) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setAvailable(isAvailable);

        return roomRepository.save(room);
    }

    public Room updateRoom(Long roomId, RoomRequest roomDto) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setTitle(roomDto.getTitle());
        room.setDescription(roomDto.getDescription());
        room.setRent(roomDto.getRent());
        room.setDeposit(roomDto.getDeposit());
        room.setAddress(roomDto.getAddress());
        room.setCity(roomDto.getCity());
        room.setLocality(roomDto.getLocality());
        room.setRoomType(RoomType.valueOf(roomDto.getRoomType()));

        List<Amenity> amenities =
                amenityRepository.findAllById(roomDto.getAmenityIds());

        room.setAmenities(amenities);

        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(roomId);
    }

    public List<Room> getRoomsByLocality(String locality) {
        return roomRepository.findByLocalityContainingIgnoreCaseAndAvailableTrue(locality);
    }

    public List<Room> getRoomsByRentRange(Double minRent, Double maxRent) {
        return roomRepository.findByRentBetweenAndAvailableTrue(minRent, maxRent);
    }

    public List<Room> filterRooms(
            String search,
            Double minRent,
            Double maxRent,
            RoomType roomType,
            String sortBy,
            String order
    ) {

        Specification<Room> specification =
                RoomSpecification.filterRooms(search, minRent, maxRent, roomType);

        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isBlank()) {

            Sort.Direction direction =
                    "desc".equalsIgnoreCase(order)
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;

            sort = Sort.by(direction, sortBy);
        }

        return roomRepository.findAll(specification, sort);
    }
}