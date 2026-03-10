package app.Ecommerce.ProductServiceApp.Controller;

import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
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
    }
