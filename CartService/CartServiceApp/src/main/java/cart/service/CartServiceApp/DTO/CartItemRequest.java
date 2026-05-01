package cart.service.CartServiceApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {

    private String productId;
    private Integer quantity;

    private String productName;
    private String productImage;
    private Double price;
}