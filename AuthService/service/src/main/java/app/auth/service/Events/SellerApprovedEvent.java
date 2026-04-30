package app.auth.service.Events;

import app.auth.service.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerApprovedEvent {
    private String message;
    private String messageStatus;
    private Long sellerId;
    private Status status;

}