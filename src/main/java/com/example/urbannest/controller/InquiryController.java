package com.example.urbannest.controller;

import com.example.urbannest.dto.InquiryPayload;
import com.example.urbannest.model.InquiryRequest;
import com.example.urbannest.model.InquiryStatus;
import com.example.urbannest.model.Room;
import com.example.urbannest.model.User;
import com.example.urbannest.service.InquiryRequestService;
import com.example.urbannest.service.RoomService;
import com.example.urbannest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryRequestService inquiryService;
    private final UserService userService;
    private final RoomService roomService;

    public InquiryController(InquiryRequestService inquiryService, UserService userService, RoomService roomService) {
        this.inquiryService = inquiryService;
        this.userService = userService;
        this.roomService = roomService;
    }

    // 1. Submit a new interest inquiry for a room
    @PostMapping
    public ResponseEntity<?> createInquiry(@RequestBody InquiryPayload payload) {
        try {
            User tenant = userService.getUserById(payload.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant account not found"));

            Room room = roomService.getRoomById(payload.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Property listing not found"));

            InquiryRequest requestEntity = InquiryRequest.builder()
                    .tenant(tenant)
                    .room(room)
                    .status(InquiryStatus.PENDING)
                    .build();

            InquiryRequest savedInquiry = inquiryService.createInquiry(requestEntity);
            return new ResponseEntity<>(savedInquiry, HttpStatus.CREATED);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // 2. Tenant View: Fetch all applications submitted by a specific tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<InquiryRequest>> getTenantInquiries(@PathVariable Long tenantId) {
        return ResponseEntity.ok(inquiryService.getRequestsForTenant(tenantId));
    }

    // 3. Landlord View: Fetch all incoming applications for a specific room listing
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<InquiryRequest>> getRoomInquiries(@PathVariable Long roomId) {
        return ResponseEntity.ok(inquiryService.getRequestsForRoom(roomId));
    }

    // 4. Update Inquiry Status (Owner accepts or rejects an application)
    // Example endpoint: PUT /api/inquiries/5/status?status=ACCEPTED
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            InquiryRequest updatedInquiry = inquiryService.updateInquiryStatus(id, status);
            return ResponseEntity.ok(updatedInquiry);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> hasInquiry(
            @RequestParam Long tenantId,
            @RequestParam Long roomId) {

        return ResponseEntity.ok(
                inquiryService.hasInquiry(tenantId, roomId)
        );
    }
}
