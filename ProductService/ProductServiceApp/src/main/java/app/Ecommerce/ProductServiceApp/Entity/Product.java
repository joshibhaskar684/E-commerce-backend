package app.Ecommerce.ProductServiceApp.Entity;

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
    private String name;
    private String categoryId;
    private String subCategory;
    private List<String> images;
    private String color;
    private Double price;
    private Double originalPrice;
    private String store;
    private Integer discount;
    private Integer quantity;
    private Integer returnDay;

    private String description;

    // Dynamic key-value pair
    private Map<String, Object> specifications;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
