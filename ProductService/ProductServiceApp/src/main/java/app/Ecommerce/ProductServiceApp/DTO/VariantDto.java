package app.Ecommerce.ProductServiceApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDto {

    private String name;   // e.g. Size, Color
    private String value;  // e.g. M, Red

    private BigDecimal price;
    private Integer quantity;

    private Boolean inStock;
}