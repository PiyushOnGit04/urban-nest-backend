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
        request.setStatus(InquiryStatus.PENDING);
        return inquiryRepository.save(request);
    }

    public List<InquiryRequest> getRequestsForTenant(Long tenantId) {

        List<InquiryRequest> inquiries =
                inquiryRepository.findByTenantId(tenantId);

        inquiries.sort(
                (a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())
        );

        return inquiries;
    }

    public List<InquiryRequest> getRequestsForRoom(Long roomId) {
        List<InquiryRequest> inquiries =
                inquiryRepository.findByRoomId(roomId);

        inquiries.sort(
                (a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())
        );

        return inquiries;
    }

    public InquiryRequest updateInquiryStatus(Long requestId, String targetStatus) {

        InquiryRequest request = inquiryRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Inquiry request not found!"));

        if (targetStatus.equalsIgnoreCase("ACCEPTED")) {

            request.setStatus(InquiryStatus.ACCEPTED);

        } else if (targetStatus.equalsIgnoreCase("REJECTED")) {

            request.setStatus(InquiryStatus.REJECTED);

        } else {

            throw new IllegalArgumentException("Invalid status update value");
        }

        return inquiryRepository.save(request);
    }
}
