package cart.service.CartServiceApp.PaymentService.Entity;

import cart.service.CartServiceApp.PaymentService.Enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_method_details")
public class PaymentMethodDetails {

    @Id
    private String id;

    private Long userId;

    private PaymentMethod paymentMethod;

    private String providerName;

    private String maskedAccountNumber;

    private String maskedCardNumber;

    private String cardNetwork;

    private String upiIdMasked;

    private Boolean isDefault;

    private Instant createdAt;

    private Instant updatedAt;
}