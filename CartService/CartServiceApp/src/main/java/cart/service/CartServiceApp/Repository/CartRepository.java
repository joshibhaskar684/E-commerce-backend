package cart.service.CartServiceApp.Repository;

import cart.service.CartServiceApp.Entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}