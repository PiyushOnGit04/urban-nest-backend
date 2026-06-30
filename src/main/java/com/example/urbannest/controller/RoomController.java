package com.example.urbannest.controller;

import com.example.urbannest.dto.RoomRequest;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomType;
import com.example.urbannest.model.User;
import com.example.urbannest.security.SecurityUser;
import com.example.urbannest.service.RoomService;
import com.example.urbannest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    // Pulls the authenticated user's DB id out of the security context
    private Long currentUserId(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return userService.getUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"))
                .getId();
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomDto, Authentication authentication) {
        try {
            Long ownerId = currentUserId(authentication);

            User owner = userService.getUserById(ownerId)
                    .orElseThrow(() -> new RuntimeException("Owner profile not found!"));

            Room savedRoom = roomService.createRoom(roomDto, owner);

            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
                roomService.filterRooms(search, minRent, maxRent, roomType, available, sortBy, order)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Room>> getRoomsByCity(@RequestParam String city) {
        return ResponseEntity.ok(roomService.getRoomsByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> toggleAvailability(
            @PathVariable Long id,
            @RequestParam boolean available,
            Authentication authentication) {
        try {
            Long requesterId = currentUserId(authentication);
            Room updatedRoom = roomService.updateRoomAvailability(id, available, requesterId);
            return ResponseEntity.ok(updatedRoom);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Room>> getRoomsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(roomService.getRoomsByOwner(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequest roomDto,
            Authentication authentication) {
        try {
            Long requesterId = currentUserId(authentication);
            Room updatedRoom = roomService.updateRoom(id, roomDto, requesterId);
            return ResponseEntity.ok(updatedRoom);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id, Authentication authentication) {
        try {
            Long requesterId = currentUserId(authentication);
            roomService.deleteRoom(id, requesterId);
            return ResponseEntity.ok("Room deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Room>> getRoomsByRentRange(
            @RequestParam Double minRent,
            @RequestParam Double maxRent) {
        return ResponseEntity.ok(roomService.getRoomsByRentRange(minRent, maxRent));
    }

    @GetMapping("/locality")
    public ResponseEntity<List<Room>> getRoomsByLocality(@RequestParam String locality) {
        return ResponseEntity.ok(roomService.getRoomsByLocality(locality));
    }
}