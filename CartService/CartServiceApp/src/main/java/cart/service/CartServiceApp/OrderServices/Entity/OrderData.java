package cart.service.CartServiceApp.OrderServices.Entity;


import cart.service.CartServiceApp.Entity.CartSummary;
import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_Data")
public class OrderData {
    private Status paymentStatus;
    private Status orderStatus;
    private ProductsdataSnapshot product;
    private CartSummary cartSummary;
    private  DeliveryAddress deliveryAddress;

}
