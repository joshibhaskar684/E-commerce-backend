package app.Ecommerce.ProductServiceApp.Controller;

import app.Ecommerce.ProductServiceApp.DTO.CategoryNode;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import app.Ecommerce.ProductServiceApp.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/category")
public class CategoryController {
    private CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategory(){
        return categoryService.getAllCategory();
    }



@GetMapping("/categories/tree")
public List<CategoryNode> getCategoryTree() {
    List<Category> categories = categoryService.getAllCategory().getBody();
    return categoryService.buildCategoryTree(categories);
}





}
