package com.example.urbannest.repository;

import com.example.urbannest.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByTenantId(Long tenantId);

    Optional<Wishlist> findByTenantIdAndRoomId(
            Long tenantId,
            Long roomId
    );

    void deleteByTenantIdAndRoomId(
            Long tenantId,
            Long roomId
    );
}
