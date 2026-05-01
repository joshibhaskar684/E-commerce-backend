package cart.service.CartServiceApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private Long userId;

    private List<CartItem> items;

    private CartSummary summary;

    private Instant updatedAt;
}