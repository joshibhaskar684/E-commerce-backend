package app.auth.service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Seller relation
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    // 🏪 Shop Info
    private String shopName;
    private String description;

    private String logoUrl;
    private String bannerUrl;

    // 📍 Location
    private String address;
    private String city;
    private String state;
    private String pincode;

    // 👁️ Visibility Control
    private boolean isActive; // true = visible, false = hidden

    // ⭐ Optional (good for future)
    private Double rating;
    private Integer totalReviews;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}