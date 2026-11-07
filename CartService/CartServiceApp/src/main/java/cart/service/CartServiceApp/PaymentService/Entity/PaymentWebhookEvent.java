package cart.service.CartServiceApp.PaymentService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_webhook_events")
public class PaymentWebhookEvent {

    @Id
    private String id;

    @Indexed(unique = true)
    private String gatewayEventId;

    private String gatewayName;

    private String paymentId;

    private String orderId;

    private String eventType;

    private String payload;

    private Boolean processed;

    private String processingError;

    private Instant receivedAt;

    private Instant processedAt;
}