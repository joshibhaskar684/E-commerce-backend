package cart.service.CartServiceApp.OrderService.Repository;

import cart.service.CartServiceApp.OrderService.Entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends MongoRepository<Orders, String> {

    // Find all orders of a user
    List<Orders> findByUserId(Long userId);

    // Find order by order number
    Optional<Orders> findByOrderNumber(String orderNumber);

    // Find orders by status
    List<Orders> findByOrderStatus(String orderStatus);

    // Find orders by payment status
    List<Orders> findByPaymentStatus(String paymentStatus);

    // Find orders of a user by status
    List<Orders> findByUserIdAndOrderStatus(Long userId, String orderStatus);

    // Check whether order number exists
    boolean existsByOrderNumber(String orderNumber);
}