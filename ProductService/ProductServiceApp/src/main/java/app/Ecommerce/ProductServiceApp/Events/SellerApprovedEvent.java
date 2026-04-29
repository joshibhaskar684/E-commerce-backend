package app.Ecommerce.ProductServiceApp.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerApprovedEvent {
    private Long sellerId;
    private String status;

    // constructor, getters, setters
}