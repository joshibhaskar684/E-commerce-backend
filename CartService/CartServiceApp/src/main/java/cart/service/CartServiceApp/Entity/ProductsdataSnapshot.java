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
@Document
public class ProductsdataSnapshot {
    @Id
    private String id;
    private Long sellerId;
    private Long ShopId;
    private Long ShopStaus;
    private Long SellerStatus;
    private String productId;
    private String productStatus;
    private Instant updateAt;

}
