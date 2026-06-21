package com.example.urbannest.service;

import com.example.urbannest.dto.RoomRequest;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomType;
import com.example.urbannest.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
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
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        room.setAvailable(isAvailable);
        return roomRepository.save(room);
    }
    // RoomService.java

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
}