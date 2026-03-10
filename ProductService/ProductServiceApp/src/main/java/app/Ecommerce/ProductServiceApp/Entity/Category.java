package app.Ecommerce.ProductServiceApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "categories")
public class Category {
    
    @Id
    private String id;

    private String name;

    // null for top-level categories
    private String parentId;

    // Optional: you can build hierarchy in backend
//    private List<Category> subCategories;
    // Getters and Setters
}