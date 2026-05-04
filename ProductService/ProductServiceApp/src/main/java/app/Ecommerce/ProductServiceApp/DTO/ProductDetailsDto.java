package app.Ecommerce.ProductServiceApp.DTO;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {

    private String id;
    private String name;
    private String brand;
    private String slug;

    private String categoryId;
    private List<String> categoryPath;

    private String description;

    private List<String> images;
    private String color;

    private BigDecimal price;
    private BigDecimal originalPrice;
    private Double discountPercentage;

    private Integer quantity;
    private Boolean inStock;

    private Integer returnDay;

    private Double averageRating;
    private Integer totalReviews;

    private Boolean freeShipping;
    private Integer deliveryDays;

    private Long shopId;
    private Long sellerId;

    private Status productStatus;

    private List<VariantDto> variants;

    private Map<String, Object> specifications;

    private Instant createdAt;
    private Instant updatedAt;
}