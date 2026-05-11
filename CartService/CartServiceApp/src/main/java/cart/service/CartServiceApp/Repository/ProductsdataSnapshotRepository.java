package cart.service.CartServiceApp.Repository;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.ProjectionInterface.ProductStatusAggregation;
import cart.service.CartServiceApp.ProjectionInterface.ProductsdataSnapshotCustomRepository;
import cart.service.CartServiceApp.ProjectionInterface.SellerInventoryAggregation;
import cart.service.CartServiceApp.ProjectionInterface.ShopInventoryAggregation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsdataSnapshotRepository
        extends MongoRepository<ProductsdataSnapshot, String>, ProductsdataSnapshotCustomRepository {

    /*
    =========================================================
    BASIC METHODS
    =========================================================
     */

    Optional<ProductsdataSnapshot> findByProductId(String productId);

    List<ProductsdataSnapshot> findBySellerId(Long sellerId);

    List<ProductsdataSnapshot> findByShopId(Long shopId);

    List<ProductsdataSnapshot> findByProductStatus(String productStatus);

    boolean existsByProductId(String productId);

    long countByShopId(Long shopId);

    long countBySellerId(Long sellerId);

    /*
    =========================================================
    CART VALIDATION METHODS
    =========================================================
     */

    // product available for cart checkout
    List<ProductsdataSnapshot> findByProductIdInAndProductStatusAndQuantityGreaterThan(
            List<String> productIds,
            String productStatus,
            Integer quantity
    );

    // active seller + active shop + active product
    List<ProductsdataSnapshot>
    findByProductIdInAndSellerStatusAndShopStatusAndProductStatus(
            List<String> productIds,
            Long sellerStatus,
            Long shopStatus,
            String productStatus
    );

    // check inventory
    Optional<ProductsdataSnapshot>
    findByProductIdAndQuantityGreaterThan(
            String productId,
            Integer quantity
    );

    /*
    =========================================================
    MULTI FILTER METHODS
    =========================================================
     */

    List<ProductsdataSnapshot>
    findBySellerIdAndShopIdAndProductStatus(
            Long sellerId,
            Long shopId,
            String productStatus
    );

    List<ProductsdataSnapshot>
    findBySellerIdAndSellerStatusAndShopStatus(
            Long sellerId,
            Long sellerStatus,
            Long shopStatus
    );

    Page<ProductsdataSnapshot>
    findByShopIdAndProductStatus(
            Long shopId,
            String productStatus,
            Pageable pageable
    );

    /*
    =========================================================
    DATE / SNAPSHOT METHODS
    =========================================================
     */

    List<ProductsdataSnapshot>
    findByUpdatedAtAfter(Instant updatedAt);

    List<ProductsdataSnapshot>
    findByUpdatedAtBetween(
            Instant start,
            Instant end
    );

    List<ProductsdataSnapshot>
    findByProductStatusAndUpdatedAtAfter(
            String productStatus,
            Instant updatedAt
    );

    /*
    =========================================================
    CUSTOM @QUERY METHODS
    =========================================================
     */

    // fetch only available products
    @Query(value = """
            {
                'productStatus' : ?0,
                'quantity' : { $gt : ?1 },
                'sellerStatus' : 1,
                'shopStatus' : 1
            }
            """)
    List<ProductsdataSnapshot> findAvailableProducts(
            String productStatus,
            Integer minQty
    );

    // get products for cart validation
    @Query(value = """
            {
                'productId' : { $in : ?0 },
                'productStatus' : 'ACTIVE',
                'quantity' : { $gt : 0 },
                'sellerStatus' : 1,
                'shopStatus' : 1
            }
            """)
    List<ProductsdataSnapshot> validateCartProducts(
            List<String> productIds
    );

    // lightweight projection query
    @Query(value = "{ 'productId' : ?0 }",
            fields = "{ 'productId' : 1, 'quantity' : 1, 'productStatus' : 1 }")
    Optional<ProductsdataSnapshot> getInventorySnapshot(
            String productId
    );

    /*
    =========================================================
    INVENTORY METHODS
    =========================================================
     */

    @Query(value = """
            {
                'quantity' : { $lte : ?0 }
            }
            """)
    List<ProductsdataSnapshot> findLowStockProducts(
            Integer threshold
    );

    @Query(value = """
            {
                'quantity' : 0,
                'productStatus' : 'ACTIVE'
            }
            """)
    List<ProductsdataSnapshot> findOutOfStockProducts();

    /*
    =========================================================
    PAGINATION + SORTING
    =========================================================
     */

    Page<ProductsdataSnapshot>
    findBySellerId(
            Long sellerId,
            Pageable pageable
    );

    Page<ProductsdataSnapshot>
    findByProductStatusAndQuantityGreaterThan(
            String status,
            Integer quantity,
            Pageable pageable
    );

    /*
    =========================================================
    AGGREGATION METHODS
    =========================================================
     */

    // total quantity by seller
    @Aggregation(pipeline = {
            "{ '$match' : { 'sellerId' : ?0 } }",
            "{ '$group' : { '_id' : '$sellerId', 'totalQty' : { '$sum' : '$quantity' } } }"
    })
    List<SellerInventoryAggregation> getTotalInventoryBySeller(Long sellerId);

    // total quantity by shop
    @Aggregation(pipeline = {
            "{ '$match' : { 'shopId' : ?0 } }",
            "{ '$group' : { '_id' : '$shopId', 'totalQty' : { '$sum' : '$quantity' } } }"
    })
    List<ShopInventoryAggregation> getTotalInventoryByShop(Long shopId);

    // count products status wise
    @Aggregation(pipeline = {
            "{ '$group' : { '_id' : '$productStatus', 'count' : { '$sum' : 1 } } }"
    })
    List<ProductStatusAggregation> countProductsByStatus();

}