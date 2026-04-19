package app.auth.service.DTO;

import app.auth.service.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerProfileDto {
    private Long id;
    private String businessName;
private String sellerEmail;
    private String mobileNo;

    private Status status;

    private LocalDateTime createdAt;

    private String address;
    private String city;
    private String state;
    private String pincode;
}
