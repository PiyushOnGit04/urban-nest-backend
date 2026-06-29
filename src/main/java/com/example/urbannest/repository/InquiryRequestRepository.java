package com.example.urbannest.repository;

import com.example.urbannest.model.InquiryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InquiryRequestRepository extends JpaRepository<InquiryRequest, Long> {

    // Get all requests submitted by a specific tenant
    List<InquiryRequest> findByTenantId(Long tenantId);

    // Get all requests for a specific room (so the owner can approve/reject)
    List<InquiryRequest> findByRoomId(Long roomId);

    boolean existsByTenantIdAndRoomId(Long tenantId, Long roomId);
}
