package com.example.urbannest.service;

import com.example.urbannest.model.Wishlist;
import com.example.urbannest.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public Wishlist save(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getUserWishlist(Long tenantId) {
        return wishlistRepository.findByTenantId(tenantId);
    }

    public boolean exists(Long tenantId, Long roomId) {
        return wishlistRepository
                .findByTenantIdAndRoomId(tenantId, roomId)
                .isPresent();
    }

    public void remove(Long tenantId, Long roomId) {
        wishlistRepository.deleteByTenantIdAndRoomId(
                tenantId,
                roomId
        );
    }
}