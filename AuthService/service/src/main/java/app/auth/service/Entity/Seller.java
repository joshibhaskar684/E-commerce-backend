package app.auth.service.Entity;

import app.auth.service.Enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetailsEntity user;

    private String businessName;

    private String mobileNo;
    @NotNull
    private String panNumber;

    private String gstNumber; // optional

    private String idProofUrl;   // uploaded document
    private String panCardUrl;   // file storage link

    // 💳 Bank Details
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;

    // 📍 Address
    private String address;
    private String city;
    private String state;
    private String pincode;

    private Status status;

    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}