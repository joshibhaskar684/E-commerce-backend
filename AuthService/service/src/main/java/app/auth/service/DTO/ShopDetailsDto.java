package app.auth.service.DTO;

import app.auth.service.Enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopDetailsDto {
    private Long id;
    private String rejectionReason;
    private String shopName;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private Status status;
    private Double rating = 0.0;
    private Integer totalReviews = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String mobileNo;
    private String businessOwnerName;
    private Status ownerStatus;
}
