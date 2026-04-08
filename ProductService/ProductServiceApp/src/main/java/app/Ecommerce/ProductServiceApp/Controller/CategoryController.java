package app.Ecommerce.ProductServiceApp.Controller;

import app.Ecommerce.ProductServiceApp.DTO.CategoryNode;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import app.Ecommerce.ProductServiceApp.Service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
public class CategoryController {
    private CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }
    @PutMapping("/category/update")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }
    @DeleteMapping("/category/delete")
    public ResponseEntity<String> deleteCategory(@RequestBody Category category){
        return categoryService.deleteCategory(category);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("/category/main")
    public ResponseEntity<Page<Category>> getPageOfMainCategory(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return categoryService.getAllMainCategory(pageno,pagesize);
    }
    @GetMapping("/category/sub-category")
    public ResponseEntity<Page<Category>> getPageOfSubCategory(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return categoryService.getAllSubCategory(pageno,pagesize);
    }





    @GetMapping("/category/tree")
public List<CategoryNode> getCategoryTree() {
    ResponseEntity<List<Category>> response = categoryService.getAllCategory();

    List<Category> categories = response.getBody();

    if (categories == null) {
        return Collections.emptyList();
    }

    return categoryService.buildCategoryTree(categories);
}





}
