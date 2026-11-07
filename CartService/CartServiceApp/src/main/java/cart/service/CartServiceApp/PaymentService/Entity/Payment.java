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
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String paymentNumber;

    private String orderId;

    private String orderNumber;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal refundedAmount;

    private String currency = "INR";

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String gatewayName;

    private String gatewayTransactionId;

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private String gatewaySignature;

    private String failureReason;

    private Instant paymentInitiatedAt;

    private Instant paymentCompletedAt;

    private Instant createdAt;

    private Instant updatedAt;
}