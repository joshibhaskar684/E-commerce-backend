package cart.service.CartServiceApp.RepositoryInterfaceImplementation;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.ProjectionInterface.ProductsdataSnapshotCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductsdataSnapshotCustomRepositoryImpl
        implements ProductsdataSnapshotCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ProductsdataSnapshot> searchProducts(
            Long sellerId,
            Long shopId,
            String productStatus,
            Integer minQty
    ) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (sellerId != null) {
            criteriaList.add(Criteria.where("sellerId").is(sellerId));
        }

        if (shopId != null) {
            criteriaList.add(Criteria.where("shopId").is(shopId));
        }

        if (productStatus != null) {
            criteriaList.add(Criteria.where("productStatus").is(productStatus));
        }

        if (minQty != null) {
            criteriaList.add(Criteria.where("quantity").gte(minQty));
        }

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(criteriaList)
            );
        }

        return mongoTemplate.find(
                query,
                ProductsdataSnapshot.class
        );
    }
}