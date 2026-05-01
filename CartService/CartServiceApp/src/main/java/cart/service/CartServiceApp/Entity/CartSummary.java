package cart.service.CartServiceApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSummary {

    private Double subTotal;       // sum(price * qty)

    private Double discount;       // coupon discount

    private Double deliveryFee;    // optional future

    private Double tax;            // optional GST

    private Double totalAmount;    // final payable

    private String currency = "INR";
}