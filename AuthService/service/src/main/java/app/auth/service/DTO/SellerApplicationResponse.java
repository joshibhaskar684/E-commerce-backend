package app.auth.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerApplicationResponse {

    private Long applicationId;
    private String status;
    private String reason;
    private LocalDateTime submissionDate;
}