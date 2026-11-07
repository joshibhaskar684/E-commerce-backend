package cart.service.CartServiceApp.PaymentService.Entity;

import cart.service.CartServiceApp.PaymentService.Enums.RefundStatus;
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
@Document(collection = "refunds")
public class Refund {

    @Id
    private String id;

    private String refundNumber;

    private String paymentId;

    private String orderId;

    private Long userId;

    private BigDecimal refundAmount;

    private String currency = "INR";

    private RefundStatus refundStatus;

    private String refundReason;

    private String gatewayName;

    private String gatewayRefundId;

    private String gatewayTransactionId;

    private String failureReason;

    private Instant requestedAt;

    private Instant processedAt;

    private Instant createdAt;

    private Instant updatedAt;
}