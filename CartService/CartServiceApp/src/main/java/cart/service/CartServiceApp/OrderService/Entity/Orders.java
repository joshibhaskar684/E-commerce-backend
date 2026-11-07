package cart.service.CartServiceApp.OrderService.Entity;

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
@Document(collection = "orders")
public class Orders {


    @Id
    private String id;

    private String orderNumber;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal shippingCharge;

    private BigDecimal taxAmount;

    private BigDecimal finalAmount;

    private String orderStatus;

    private String paymentStatus;

    private String paymentMethod;

    private String paymentId;

    private Long shippingAddressId;

    // Address snapshot
    private Order_Address_Snapshot addressSnapshot;

    // Purchased products
    private List<Order_Items> items;

    // Order status history
    private List<Order_Status_History> statusHistory;

    private Instant createdAt;

    private Instant updatedAt;
}
