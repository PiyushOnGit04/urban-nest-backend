package com.example.urbannest.controller;

import com.example.urbannest.dto.UpdateProfileRequest;
import com.example.urbannest.model.User;
import com.example.urbannest.security.SecurityUser;
import com.example.urbannest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setPassword(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserProfileByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(user -> {
                    user.setPassword(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateOwnProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        try {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            Long userId = securityUser.getUser().getId();

            User updated = userService.updateProfile(
                    userId,
                    request.getName(),
                    request.getPhoneNumber()
            );

            updated.setPassword(null);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}