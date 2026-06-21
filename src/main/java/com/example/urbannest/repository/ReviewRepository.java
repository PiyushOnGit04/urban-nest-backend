package com.example.urbannest.repository;

import com.example.urbannest.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Fetch all reviews for a room to show on the details page
    List<Review> findByRoomId(Long roomId);
}
