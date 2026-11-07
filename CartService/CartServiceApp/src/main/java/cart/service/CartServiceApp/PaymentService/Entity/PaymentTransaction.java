package cart.service.CartServiceApp.PaymentService.Entity;

import cart.service.CartServiceApp.PaymentService.Enums.PaymentMethod;
import cart.service.CartServiceApp.PaymentService.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_transactions")
public class PaymentTransaction {

    @Id
    private String id;

    private String paymentId;

    private String orderId;

    private Long userId;

    private String transactionNumber;

    private PaymentMethod paymentMethod;

    private PaymentStatus transactionStatus;

    private BigDecimal amount;

    private String currency = "INR";

    private String gatewayName;

    private String gatewayTransactionId;

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private String failureCode;

    private String failureMessage;

    private Instant initiatedAt;

    private Instant completedAt;

    private Instant createdAt;
}