package app.Ecommerce.ProductServiceApp.Repository;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerDataRepository extends MongoRepository<SellerData,String> {
}
