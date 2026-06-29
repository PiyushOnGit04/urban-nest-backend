package com.example.urbannest.controller;

import com.example.urbannest.dto.RoomRequest;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomType;
import com.example.urbannest.model.User;
import com.example.urbannest.service.RoomService;
import com.example.urbannest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    // 1. Create a new room listing
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomDto) {

        try {

            User owner = userService.getUserById(roomDto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner profile not found!"));

            Room savedRoom = roomService.createRoom(roomDto, owner);

            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Get all available rooms globally
    @GetMapping
    public ResponseEntity<List<Room>> getRooms(

            @RequestParam(required = false) String search,

            @RequestParam(required = false) Double minRent,

            @RequestParam(required = false) Double maxRent,

            @RequestParam(required = false) RoomType roomType,

            @RequestParam(required = false) Boolean available,

            @RequestParam(required = false) String sortBy,

            @RequestParam(defaultValue = "asc") String order

    ) {

        return ResponseEntity.ok(
                roomService.filterRooms(
                        search,
                        minRent,
                        maxRent,
                        roomType,
                        available,
                        sortBy,
                        order
                )
        );
    }

    // 3. Search rooms by City (Perfect for your Flutter app search bar)
    @GetMapping("/search")
    public ResponseEntity<List<Room>> getRoomsByCity(@RequestParam String city) {
        return ResponseEntity.ok(roomService.getRoomsByCity(city));
    }

    // 4. View detailed breakdown of a single room listing
    @GetMapping ("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 5. Update room status (Mark as rented out or available)
    @PutMapping("/{id}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id, @RequestParam boolean available) {
        try {
            Room updatedRoom = roomService.updateRoomAvailability(id, available);
            return ResponseEntity.ok(updatedRoom);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // RoomController.java

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Room>> getRoomsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(roomService.getRoomsByOwner(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequest roomDto) {

        try {
            Room updatedRoom = roomService.updateRoom(id, roomDto);
            return ResponseEntity.ok(updatedRoom);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Room>> getRoomsByRentRange(
            @RequestParam Double minRent,
            @RequestParam Double maxRent) {

        return ResponseEntity.ok(
                roomService.getRoomsByRentRange(minRent, maxRent)
        );
    }

    @GetMapping("/locality")
    public ResponseEntity<List<Room>> getRoomsByLocality(
            @RequestParam String locality) {

        return ResponseEntity.ok(
                roomService.getRoomsByLocality(locality)
        );
    }
}
