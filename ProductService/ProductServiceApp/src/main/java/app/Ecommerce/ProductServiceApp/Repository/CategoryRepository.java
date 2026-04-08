package app.Ecommerce.ProductServiceApp.Repository;

import app.Ecommerce.ProductServiceApp.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category,String> {

    Page<Category> findByParentIdIsNull(Pageable pageable);
    Page<Category> findByParentIdIsNotNull(Pageable pageable);

}
