package app.Ecommerce.ProductServiceApp.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Double price;
    @Positive
    private Double originalPrice;
    private String storeId;
    private Integer quantity;
    private Integer returnDay;

    private String description;

    private List<String> categoryPath;


    // Dynamic key-value pair
    private Map<String, Object> specifications;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
