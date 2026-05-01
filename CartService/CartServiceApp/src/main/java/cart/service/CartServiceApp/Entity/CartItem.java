package cart.service.CartServiceApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cart_items")
public class CartItem {

    @Id
    private String id;

    private Long userId;          // from User Service
    private String productId;     // from Product Service

    private Integer quantity;

    // Optional snapshot (recommended for speed)
    private String productName;
    private String productImage;
    private Double priceAtAddTime;
    private Double price;
    private Instant createdAt;
    private Instant updatedAt;
}