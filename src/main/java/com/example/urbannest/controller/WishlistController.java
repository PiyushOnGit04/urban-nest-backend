package com.example.urbannest.controller;

import com.example.urbannest.dto.WishlistPayload;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.User;
import com.example.urbannest.model.Wishlist;
import com.example.urbannest.service.RoomService;
import com.example.urbannest.service.UserService;
import com.example.urbannest.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final RoomService roomService;

    public WishlistController(
            WishlistService wishlistService,
            UserService userService,
            RoomService roomService
    ) {
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<?> addToWishlist(
            @RequestBody WishlistPayload payload
    ) {

        if (wishlistService.exists(
                payload.getTenantId(),
                payload.getRoomId()
        )) {

            return ResponseEntity.badRequest()
                    .body("Room already saved");
        }

        User tenant = userService.getUserById(
                payload.getTenantId()
        ).orElseThrow();

        Room room = roomService.getRoomById(
                payload.getRoomId()
        ).orElseThrow();

        Wishlist wishlist = Wishlist.builder()
                .tenant(tenant)
                .room(room)
                .build();

        return ResponseEntity.ok(
                wishlistService.save(wishlist)
        );
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<List<Wishlist>> getWishlist(
            @PathVariable Long tenantId
    ) {
        return ResponseEntity.ok(
                wishlistService.getUserWishlist(tenantId)
        );
    }



    @DeleteMapping
    public ResponseEntity<?> removeWishlist(
            @RequestParam Long tenantId,
            @RequestParam Long roomId
    ) {

        wishlistService.remove(
                tenantId,
                roomId
        );

        return ResponseEntity.ok(
                "Removed successfully"
        );
    }
}