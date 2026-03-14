package app.auth.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDTO {
    private String name;
    private String mobileno;
    private String password;
    private String email;
    private String role;
}
