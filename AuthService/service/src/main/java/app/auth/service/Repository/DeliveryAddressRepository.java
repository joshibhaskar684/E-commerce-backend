package app.auth.service.Repository;

import app.auth.service.Entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository 
        extends JpaRepository<DeliveryAddress, Long> {

    // Get all addresses of a profile
    List<DeliveryAddress> findByDeliveryProfileId(Long deliveryProfileId);

    // Get default address
    Optional<DeliveryAddress> findByDeliveryProfileIdAndIsDefaultTrue(Long deliveryProfileId);

    // Get addresses by type (HOME / OFFICE)
    List<DeliveryAddress> findByDeliveryProfileIdAndAddressType(
            Long deliveryProfileId,
            String addressType
    );

    // Count addresses for a user (useful for limits like max 5 addresses)
    long countByDeliveryProfileId(Long deliveryProfileId);

    // Delete all addresses for a profile (useful when deleting user)
    void deleteByDeliveryProfileId(Long deliveryProfileId);

    // Optional: find specific address of a profile
    Optional<DeliveryAddress> findByIdAndDeliveryProfileId(Long id, Long deliveryProfileId);
}