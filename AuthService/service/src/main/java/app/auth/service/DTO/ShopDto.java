package app.auth.service.DTO;

import app.auth.service.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopDto {
    private Long id;

    private String businessName;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
