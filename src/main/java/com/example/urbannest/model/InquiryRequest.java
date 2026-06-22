package com.example.urbannest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inquiry_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"tenant_id", "room_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    @JsonIgnoreProperties({"rooms","inquiries","reviews","password"})
    private User tenant;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "room_id", nullable = false)
   @JsonIgnoreProperties({"inquiries","reviews"})
   private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = InquiryStatus.PENDING;
        }
    }
}
