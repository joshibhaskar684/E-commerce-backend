package cart.service.CartServiceApp.Service;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.ProjectionInterface.ProductStatusAggregation;
import cart.service.CartServiceApp.ProjectionInterface.SellerInventoryAggregation;
import cart.service.CartServiceApp.ProjectionInterface.ShopInventoryAggregation;
import cart.service.CartServiceApp.Repository.ProductsdataSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductsdataSnapshotService {

    private final ProductsdataSnapshotRepository repository;

    /*
    =========================================================
    SAVE METHODS
    =========================================================
     */

    public ProductsdataSnapshot saveProductSnapshot(
            ProductsdataSnapshot snapshot
    ) {
        return repository.save(snapshot);
    }

    public List<ProductsdataSnapshot> saveAllSnapshots(
            List<ProductsdataSnapshot> snapshots
    ) {
        return repository.saveAll(snapshots);
    }

    /*
    =========================================================
    BASIC FETCH METHODS
    =========================================================
     */

    public Optional<ProductsdataSnapshot> getById(String id) {
        return repository.findById(id);
    }

    public Optional<ProductsdataSnapshot> getByProductId(
            String productId
    ) {
        return repository.findByProductId(productId);
    }

    public List<ProductsdataSnapshot> getBySellerId(
            Long sellerId
    ) {
        return repository.findBySellerId(sellerId);
    }

    public List<ProductsdataSnapshot> getByShopId(
            Long shopId
    ) {
        return repository.findByShopId(shopId);
    }

    public List<ProductsdataSnapshot> getByProductStatus(
            String productStatus
    ) {
        return repository.findByProductStatus(productStatus);
    }

    public boolean existsByProductId(String productId) {
        return repository.existsByProductId(productId);
    }

    public long countByShopId(Long shopId) {
        return repository.countByShopId(shopId);
    }

    public long countBySellerId(Long sellerId) {
        return repository.countBySellerId(sellerId);
    }

    /*
    =========================================================
    CART VALIDATION METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    getAvailableProductsForCart(
            List<String> productIds,
            String productStatus,
            Integer quantity
    ) {

        return repository
                .findByProductIdInAndProductStatusAndQuantityGreaterThan(
                        productIds,
                        productStatus,
                        quantity
                );
    }

    public List<ProductsdataSnapshot>
    validateSellerShopAndProducts(
            List<String> productIds,
            Long sellerStatus,
            Long shopStatus,
            String productStatus
    ) {

        return repository
                .findByProductIdInAndSellerStatusAndShopStatusAndProductStatus(
                        productIds,
                        sellerStatus,
                        shopStatus,
                        productStatus
                );
    }

    public Optional<ProductsdataSnapshot>
    checkInventory(
            String productId,
            Integer quantity
    ) {

        return repository
                .findByProductIdAndQuantityGreaterThan(
                        productId,
                        quantity
                );
    }

    /*
    =========================================================
    MULTI FILTER METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    getBySellerShopAndStatus(
            Long sellerId,
            Long shopId,
            String productStatus
    ) {

        return repository
                .findBySellerIdAndShopIdAndProductStatus(
                        sellerId,
                        shopId,
                        productStatus
                );
    }

    public List<ProductsdataSnapshot>
    getBySellerAndStatuses(
            Long sellerId,
            Long sellerStatus,
            Long shopStatus
    ) {

        return repository
                .findBySellerIdAndSellerStatusAndShopStatus(
                        sellerId,
                        sellerStatus,
                        shopStatus
                );
    }

    public Page<ProductsdataSnapshot>
    getShopProductsWithPagination(
            Long shopId,
            String productStatus,
            Pageable pageable
    ) {

        return repository
                .findByShopIdAndProductStatus(
                        shopId,
                        productStatus,
                        pageable
                );
    }

    /*
    =========================================================
    DATE METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    getRecentlyUpdatedProducts(
            Instant updatedAt
    ) {

        return repository.findByUpdatedAtAfter(updatedAt);
    }

    public List<ProductsdataSnapshot>
    getProductsBetweenDates(
            Instant start,
            Instant end
    ) {

        return repository.findByUpdatedAtBetween(start, end);
    }

    public List<ProductsdataSnapshot>
    getUpdatedActiveProducts(
            String status,
            Instant updatedAt
    ) {

        return repository
                .findByProductStatusAndUpdatedAtAfter(
                        status,
                        updatedAt
                );
    }

    /*
    =========================================================
    CUSTOM QUERY METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    getAvailableProducts(
            String productStatus,
            Integer minQty
    ) {

        return repository.findAvailableProducts(
                productStatus,
                minQty
        );
    }

    public List<ProductsdataSnapshot>
    validateCartProducts(
            List<String> productIds
    ) {

        return repository.validateCartProducts(productIds);
    }

    public Optional<ProductsdataSnapshot>
    getInventorySnapshot(
            String productId
    ) {

        return repository.getInventorySnapshot(productId);
    }

    /*
    =========================================================
    INVENTORY METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    getLowStockProducts(
            Integer threshold
    ) {

        return repository.findLowStockProducts(threshold);
    }

    public List<ProductsdataSnapshot>
    getOutOfStockProducts() {

        return repository.findOutOfStockProducts();
    }

    /*
    =========================================================
    PAGINATION METHODS
    =========================================================
     */

    public Page<ProductsdataSnapshot>
    getSellerProducts(
            Long sellerId,
            Pageable pageable
    ) {

        return repository.findBySellerId(
                sellerId,
                pageable
        );
    }

    public Page<ProductsdataSnapshot>
    getProductsByStatusAndQuantity(
            String status,
            Integer quantity,
            Pageable pageable
    ) {

        return repository
                .findByProductStatusAndQuantityGreaterThan(
                        status,
                        quantity,
                        pageable
                );
    }

    /*
    =========================================================
    AGGREGATION METHODS
    =========================================================
     */

    public List<SellerInventoryAggregation>
    getTotalInventoryBySeller(
            Long sellerId
    ) {

        return repository.getTotalInventoryBySeller(
                sellerId
        );
    }

    public List<ShopInventoryAggregation>
    getTotalInventoryByShop(
            Long shopId
    ) {

        return repository.getTotalInventoryByShop(
                shopId
        );
    }

    public List<ProductStatusAggregation>
    countProductsByStatus() {

        return repository.countProductsByStatus();
    }

    /*
    =========================================================
    DYNAMIC SEARCH METHODS
    =========================================================
     */

    public List<ProductsdataSnapshot>
    searchProducts(
            Long sellerId,
            Long shopId,
            String productStatus,
            Integer minQty
    ) {

        return repository.searchProducts(
                sellerId,
                shopId,
                productStatus,
                minQty
        );
    }

    /*
    =========================================================
    DELETE METHODS
    =========================================================
     */

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}