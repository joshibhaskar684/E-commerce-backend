package cart.service.CartServiceApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCartRequest {

    private String productId;
    private Integer quantity;
}