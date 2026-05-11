package cart.service.CartServiceApp.ProjectionInterface;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;

import java.util.List;

public interface ProductsdataSnapshotCustomRepository {

    List<ProductsdataSnapshot> searchProducts(
            Long sellerId,
            Long shopId,
            String productStatus,
            Integer minQty
    );
}