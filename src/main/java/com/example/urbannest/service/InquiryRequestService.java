package com.example.urbannest.service;

import com.example.urbannest.model.InquiryRequest;
import com.example.urbannest.model.InquiryStatus;
import com.example.urbannest.repository.InquiryRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InquiryRequestService {

    private final InquiryRequestRepository inquiryRepository;

    public InquiryRequestService(InquiryRequestRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public InquiryRequest createInquiry(InquiryRequest request) {
        request.setStatus(InquiryStatus.PENDING); // Enforce initial status programmatically
        return inquiryRepository.save(request);
    }

    public List<InquiryRequest> getRequestsForTenant(Long tenantId) {
        return inquiryRepository.findByTenantId(tenantId);
    }

    public List<InquiryRequest> getRequestsForRoom(Long roomId) {
        return inquiryRepository.findByRoomId(roomId);
    }

    public InquiryRequest updateInquiryStatus(Long requestId, String targetStatus) {
        InquiryRequest request = inquiryRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Inquiry request not found!"));

        // Ensure status updates match expected states
        if (targetStatus.equalsIgnoreCase("ACCEPTED") || targetStatus.equalsIgnoreCase("REJECTED")) {
            request.setStatus(InquiryStatus.ACCEPTED);
        } else {
            throw new IllegalArgumentException("Invalid status update value provided");
        }

        return inquiryRepository.save(request);
    }
}