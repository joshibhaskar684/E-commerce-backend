package app.Ecommerce.ProductServiceApp.ElasticSearch.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Document(indexName = "products")
@Data
public class ProductDocument {

    @Id
    private String id;

    private String name;

    private String brand;

    private String categoryId;

    private BigDecimal price;

    private String color;

    private String slug;

    private String description;

    private List<String> images;

    private Long sellerId;

    private Long shopId;

    private List<String> categoryPath;
}