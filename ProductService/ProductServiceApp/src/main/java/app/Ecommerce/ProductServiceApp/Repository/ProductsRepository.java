package app.Ecommerce.ProductServiceApp.Repository;

import app.Ecommerce.ProductServiceApp.Entity.Product;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductsRepository extends MongoRepository<Product ,String> {
    List<Product>findByCategoryId(String categoryId);


    Page<Product> findAllBySellerId(Long sellerId, Pageable pageable);

    Page<Product> findAllByShopId(Long shopId, Pageable pageable);

    Page<Product> findAllByShopIdAndProductStatus(Long shopId, Status status, Pageable pageable);

    Page<Product> findAllBySellerIdAndProductStatus(Long sellerId, Status status, Pageable pageable);

    long countBySellerId(Long sellerId);
    long countByShopId(Long shopId);
    long countBySellerIdAndProductStatus(Long sellerId, Status status);
    long countByShopIdAndProductStatus(Long shopId, Status status);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByPriceBetween(Double min, Double max, Pageable pageable);

    Page<Product> findByProductStatus(Status status, Pageable pageable);



}
