package cart.service.CartServiceApp.OrderServices.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {

    @Id
    private Long id;

    private String fullName;
    private String mobile;

    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;

    private String addressType; // HOME / OFFICE / OTHER

    private Boolean isDefault = false;
}