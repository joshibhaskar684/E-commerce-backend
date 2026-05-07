package app.Ecommerce.ProductServiceApp.Repository;

import app.Ecommerce.ProductServiceApp.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category,String> {

    Optional<Category> findByName(String s);

    Page<Category> findByParentIdIsNull(Pageable pageable);
    Page<Category> findByParentIdIsNotNull(Pageable pageable);

}
