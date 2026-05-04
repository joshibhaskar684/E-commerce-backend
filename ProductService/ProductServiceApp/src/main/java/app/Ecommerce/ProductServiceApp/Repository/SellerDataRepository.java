package app.Ecommerce.ProductServiceApp.Repository;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellerDataRepository extends MongoRepository<SellerData,String> {
    // 1. Check if seller + shop mapping exists
    Optional<SellerData> findBySellerIdAndShopId(Long sellerId, Long shopId);

    // 2. Check seller status (approved / suspended / rejected etc.)
    List<SellerData> findBySellerIdAndStatus(Long sellerId, Status status);

    // 3. Check shop status under a seller
    Optional<SellerData> findBySellerIdAndShopIdAndStatus(
            Long sellerId,
            Long shopId,
            Status status
    );



}
