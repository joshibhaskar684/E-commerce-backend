package app.auth.service.Repository;

import app.auth.service.Entity.Seller;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller>findByUser(UserDetailsEntity user);

    Page<Seller> findAllByStatus(Status status, Pageable pageable);
    List<Seller> findAllByStatus(Status status);
    long countByStatus(Status status);
}
