package app.Ecommerce.ProductServiceApp.Entity;

import app.Ecommerce.ProductServiceApp.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seller_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerData {
    
    private Long sellerId;
    private Long shopId;
    private Status status;
   
}