package cart.service.CartServiceApp.PaymentService.Entity;

import cart.service.CartServiceApp.PaymentService.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_status_history")
public class PaymentStatusHistory {

    @Id
    private String id;

    private String paymentId;

    private String orderId;

    private PaymentStatus oldStatus;

    private PaymentStatus newStatus;

    private String remarks;

    private String source;

    private Instant changedAt;
}