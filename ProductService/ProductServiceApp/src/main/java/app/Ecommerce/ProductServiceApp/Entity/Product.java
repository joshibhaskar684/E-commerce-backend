package app.Ecommerce.ProductServiceApp.Entity;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    private String brand;
    @NotNull
    private String name;
    @NotBlank
    private String categoryId;
//    private String subCategory;
    private List<String> images;
    private String color;
    @Positive
    private BigDecimal price;
    @Positive
    private BigDecimal originalPrice;
    private Integer quantity;
    private Integer returnDay;

    private String description;
//    private Boolean inStock=quantity>0;

    private Double averageRating;
    private Integer totalReviews;

//    private Double discountPercentage;

    private Boolean freeShipping;
    private Integer deliveryDays;
    @Indexed
    private String slug;
//    private List<Variant> variants;
    private List<String> categoryPath;


    @Indexed
    private Long shopId;
    @Indexed// PK
    private Long sellerId;

    @Indexed
    private Status productStatus;
    // Dynamic key-value pair
    private Map<String, Object> specifications;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
