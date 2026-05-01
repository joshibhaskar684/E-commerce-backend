package app.auth.service.Repository;

import app.auth.service.Entity.DeliveryProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryProfileRepository 
        extends JpaRepository<DeliveryProfile, Long> {

    // Find profile by user
    Optional<DeliveryProfile> findByUserId(Long userId);

    // Check if profile exists for user
    boolean existsByUserId(Long userId);

    // Delete by user (useful during account deletion)
    void deleteByUserId(Long userId);
}