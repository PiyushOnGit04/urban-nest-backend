package com.example.urbannest.service;

import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomImage;
import com.example.urbannest.repository.RoomImageRepository;
import com.example.urbannest.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomImageService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final CloudinaryService cloudinaryService;

    public RoomImageService(
            RoomRepository roomRepository,
            RoomImageRepository roomImageRepository,
            CloudinaryService cloudinaryService) {

        this.roomRepository = roomRepository;
        this.roomImageRepository = roomImageRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<RoomImage> uploadRoomImages(
            Long roomId,
            List<MultipartFile> files) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        List<RoomImage> uploadedImages = new ArrayList<>();

        try {

            for (int i = 0; i < files.size(); i++) {

                MultipartFile file = files.get(i);

                Map<?, ?> uploadResult =
                        cloudinaryService.uploadFile(file);

                RoomImage roomImage = RoomImage.builder()
                        .room(room)
                        .imageUrl(
                                uploadResult.get("secure_url").toString()
                        )
                        .publicId(
                                uploadResult.get("public_id").toString()
                        )
                        .coverImage(i == 0)
                        .build();

                uploadedImages.add(
                        roomImageRepository.save(roomImage)
                );
            }

            return uploadedImages;

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }
    }
}
