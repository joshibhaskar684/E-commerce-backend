package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.DTO.CategoryNode;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Category> createCategory(Category category){
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
    }
    public ResponseEntity<List<Category>> getAllCategory(){
        return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);

    }
    public List<CategoryNode> buildCategoryTree(List<Category> categories) {
        Map<String,CategoryNode> map=new HashMap<>();
        for(Category category:categories){
            map.put(category.getId(),new CategoryNode(category.getId(),category.getName(),new ArrayList<>()));
        }
        List<CategoryNode> roots=new ArrayList<>();
        for(Category category:categories){
            if(category.getParentId()==null){
                roots.add(map.get(category.getId()));
            }
            else{
                CategoryNode parent=map.get(category.getParentId());
                if(parent!=null){
                    parent.getChildren().add(map.get(category.getId()));
                }
            }
        }
        return roots;


    }

}
