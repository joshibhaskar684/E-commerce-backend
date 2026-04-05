package app.Ecommerce.ProductServiceApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryNode {
    private String id;
    private String name;
    private List<CategoryNode> children;
}