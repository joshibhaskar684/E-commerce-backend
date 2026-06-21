package cart.service.CartServiceApp.Service;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.Repository.ProductsdataSnapshotRepository;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ProductsEvent;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ConsumerHelperForProductEventConsumer {
    private final ProductsdataSnapshotRepository productsdataSnapshotRepository;
    private MongoTemplate mongoTemplate;

    public ConsumerHelperForProductEventConsumer(ProductsdataSnapshotRepository productsdataSnapshotRepository, MongoTemplate mongoTemplate) {
        this.productsdataSnapshotRepository = productsdataSnapshotRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void CreateProductDataSnapShot(ProductsEvent event){
        if(productsdataSnapshotRepository.existsByProductId(event.getProductId())){
            throw new RuntimeException("Product Already Exists");
        }
        ProductsdataSnapshot productsdataSnapshot=new ProductsdataSnapshot();
        productsdataSnapshot.setImages(event.getImages());
        productsdataSnapshot.setColor(event.getColor());
        productsdataSnapshot.setName(event.getName());
        productsdataSnapshot.setOriginalPrice(event.getOriginalPrice());
        productsdataSnapshot.setPrice(productsdataSnapshot.getPrice());
        productsdataSnapshot.setProductId(event.getProductId());
        productsdataSnapshot.setQuantity(event.getQuantity());
        productsdataSnapshot.setSellerId(event.getSellerId());
        productsdataSnapshot.setShopId(event.getShopId());
        productsdataSnapshot.setShopStaus(event.getShopStatus());
        productsdataSnapshot.setSellerStatus(event.getSellerStatus());
        productsdataSnapshot.setUpdateAt(event.getUpdateAt());
        productsdataSnapshot.setProductStatus(event.getProductStatus());
        productsdataSnapshotRepository.save(productsdataSnapshot);
    }

    public void UpdateProductDataSnapshot(ProductsEvent event){
        ProductsdataSnapshot productsdataSnapshot=productsdataSnapshotRepository.findByProductId(event.getProductId()).orElseThrow(()->new RuntimeException("Product Not Found"));
        if(event.getQuantity()!=null){
            productsdataSnapshot.setQuantity(event.getQuantity());
        }
        productsdataSnapshot.setProductStatus(event.getProductStatus());
        productsdataSnapshot.setUpdateAt(event.getUpdateAt());
        productsdataSnapshotRepository.save(productsdataSnapshot);

    }
    public void DeleteProductDataSnapshot(ProductsEvent event){
        ProductsdataSnapshot productsdataSnapshot=productsdataSnapshotRepository.findByProductId(event.getProductId()).orElseThrow(()->new RuntimeException("Product Not Found"));
        productsdataSnapshotRepository.deleteById(event.getProductId());

    }
    public long suspendShopAndChangeStatus(Long shopId, Status oldStatus,Status newStatus){
        Query query = new Query();
        query.addCriteria(Criteria.where("shopId").is(shopId)
                .and("productStatus").is(oldStatus));

        Update update = new Update();
        update.set("productStatus", newStatus);

        UpdateResult result = mongoTemplate.updateMulti(query, update, ProductsdataSnapshot.class);

        return result.getModifiedCount();
    }
    public long suspendSellerAndChangeStatus(Long sellerId, Status oldStatus,Status newStatus){
        Query query = new Query();
        query.addCriteria(Criteria.where("sellerId").is(sellerId)
                .and("productStatus").is(oldStatus));

        Update update = new Update();
        update.set("productStatus", newStatus);

        UpdateResult result = mongoTemplate.updateMulti(query, update, ProductsdataSnapshot.class);

        return result.getModifiedCount();
    }



}
