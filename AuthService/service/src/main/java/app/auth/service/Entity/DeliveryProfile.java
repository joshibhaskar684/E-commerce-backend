package app.auth.service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_profile")
public class DeliveryProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one with User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserDetailsEntity user;

    private String defaultAddressLabel; // e.g. HOME, OFFICE

    private String preferredDeliveryTime; // optional

    private Boolean active = true;
}