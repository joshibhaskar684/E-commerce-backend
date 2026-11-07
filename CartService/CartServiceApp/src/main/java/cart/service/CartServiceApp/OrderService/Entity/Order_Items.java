package cart.service.CartServiceApp.OrderService.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order_Items {

    private String id;

    private String productId;

    private Long sellerId;

    private Long shopId;

    // Product snapshot
    private String productName;

    private List<String> productImages;

    private String color;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer quantity;

    private BigDecimal discountAmount;

    private BigDecimal subtotal;

    private Instant createdAt;
}
