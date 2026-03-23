package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import app.Ecommerce.ProductServiceApp.Repository.ProductsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProductsService {

    private  CategoryRepository categoryRepository;
    private ProductsRepository productsRepository;

    public ProductsService(CategoryRepository categoryRepository, ProductsRepository productsRepository) {
        this.categoryRepository = categoryRepository;
        this.productsRepository = productsRepository;
    }

    public ResponseEntity<Product> createNewProduct(Product product, String category){
        Category category1= categoryRepository.findById(category).orElseThrow(()->new RuntimeException("Category with id doesnt exist"));
        return new ResponseEntity<>(productsRepository.save(product), HttpStatus.OK);
    }
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productsRepository.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<List<Product>> getAllProductsByCategoryId(String categoryId){
        return new ResponseEntity<>(productsRepository.findByCategoryId(categoryId), HttpStatus.OK);
    }
    public ResponseEntity<Product>getProductDetailById(String productId){
        return new ResponseEntity<>(productsRepository.findById(productId).orElseThrow(()->new RuntimeException("product with id not found")),HttpStatus.OK);
    }

}
