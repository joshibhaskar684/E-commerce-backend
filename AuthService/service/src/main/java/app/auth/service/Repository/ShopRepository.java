package app.auth.service.Repository;

import app.auth.service.Entity.Seller;
import app.auth.service.Entity.Shop;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {

    List<Shop> findBySeller(Seller seller);

//    Optional<Shop>findBySeller(UserDetailsEntity user);

    Page<Shop> findAllByStatus(Status status, Pageable pageable);

    List<Shop> findAllByStatus(Status status);

    long countByStatus(Status status);

    long countByStatusAndSeller(Status status,Seller seller);

}
