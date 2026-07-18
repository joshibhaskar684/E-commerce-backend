package cart.service.CartServiceApp.Entity;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ProductsdataSnapshot {
    @Id
    private String id;

    private List<String> images;

    private String color;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String name;
    private Long sellerId;
    private Long shopId;
    private Status shopStatus;
    private Status sellerStatus;
    private String productId;
    private Status productStatus;
    private Instant updatedAt;
    private Integer quantity;

}
