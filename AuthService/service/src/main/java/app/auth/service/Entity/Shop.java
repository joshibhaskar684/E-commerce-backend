package app.auth.service.Entity;

import app.auth.service.Enums.Status;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

   @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_data")
    private UserDetailsEntity admin;

    @Lob
    private String rejectionReason;

    // 🏪 Shop Info
    @Column(nullable = false)
    private String shopName;
   @Lob
    private String description;

    private String logoUrl;
    private String bannerUrl;

    private String address;
    private String city;
    private String state;
    private String pincode;

    // 👁️ Visibility Control
@Enumerated(EnumType.STRING)
private Status status = Status.PENDING; // true = visible, false = hidden

    // ⭐ Optional (good for future)

    private Double rating = 0.0;
    private Integer totalReviews = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}