package com.example.urbannest.controller;

import com.example.urbannest.model.RoomImage;
import com.example.urbannest.service.RoomImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomImageController {

    private final RoomImageService roomImageService;

    public RoomImageController(RoomImageService roomImageService) {
        this.roomImageService = roomImageService;
    }

    @PostMapping("/{roomId}/images")
    public ResponseEntity<List<RoomImage>> uploadImages(
            @PathVariable Long roomId,
            @RequestParam("files") List<MultipartFile> files) {

        return ResponseEntity.ok(
                roomImageService.uploadRoomImages(roomId, files)
        );
    }
}
