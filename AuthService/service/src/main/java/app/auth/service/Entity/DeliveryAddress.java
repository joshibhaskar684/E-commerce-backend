package app.auth.service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_address")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many addresses belong to one profile
    @ManyToOne
    @JoinColumn(name = "delivery_profile_id", nullable = false)
    private DeliveryProfile deliveryProfile;

    private String fullName;
    private String mobile;

    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;

    private String addressType; // HOME / OFFICE / OTHER

    private Boolean isDefault = false;
}