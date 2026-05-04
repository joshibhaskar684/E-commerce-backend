package app.auth.service.Repository;

import app.auth.service.Entity.Seller;
import app.auth.service.Entity.Shop;
import app.auth.service.Entity.UserDetailsEntity;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {

    List<Shop> findBySeller(Seller seller);

//    Optional<Shop>findBySeller(UserDetailsEntity user);

    Page<Shop> findAllByStatus(Status status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Shop s SET s.status = :newStatus WHERE s.seller.id = :sellerId AND s.status = :currentStatus")
    int setStatusSuspendedWhereSellerIdAndStatus(
            @Param("sellerId") Long sellerId,
            @Param("currentStatus") Status currentStatus,
            @Param("newStatus") Status newStatus
    );

    Page<Shop> findByStatusAndSeller_Status(Status status, Status sellerStatus, Pageable pageable);
    List<Shop> findAllByStatus(Status status);

    long countByStatus(Status status);

    long countByStatusAndSeller(Status status,Seller seller);

}
