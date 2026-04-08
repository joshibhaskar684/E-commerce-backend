package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.DTO.CategoryNode;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if(category.getParentId()!=null&&!categoryRepository.existsById(category.getParentId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
    }
    public ResponseEntity<Category> updateCategory(Category category){
         if(category.getParentId()==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Category category1=categoryRepository.findById(category.getParentId()).orElseThrow(()->new RuntimeException("Category with Id Not Found"));
         category1.setName(category.getName());
        return new ResponseEntity<>(categoryRepository.save(category1), HttpStatus.OK);
    }
    public ResponseEntity<String> deleteCategory(Category category){
        if(category.getParentId()==null&&categoryRepository.existsById(category.getParentId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        categoryRepository.deleteById(category.getParentId());

        return new ResponseEntity<>("Category Deleted Sucessfully ", HttpStatus.OK);
    }


    public ResponseEntity<List<Category>> getAllCategory(){
        return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
    }
    public ResponseEntity<Page<Category>> getAllMainCategory(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        return new ResponseEntity<>(categoryRepository.findByParentIdIsNull(pageable),HttpStatus.OK);
    }
    public ResponseEntity<Page<Category>> getAllSubCategory(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        return new ResponseEntity<>(categoryRepository.findByParentIdIsNotNull(pageable),HttpStatus.OK);
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
