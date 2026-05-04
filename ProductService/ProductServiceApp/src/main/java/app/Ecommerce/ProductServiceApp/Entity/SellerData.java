package app.Ecommerce.ProductServiceApp.Entity;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seller_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerData {
    @Id
    private String id;
    private Long sellerId;
    private Long shopId;
    private Status status;
   
}