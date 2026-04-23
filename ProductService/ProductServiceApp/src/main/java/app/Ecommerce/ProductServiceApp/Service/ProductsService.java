package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Mapper.ProductMapper;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import app.Ecommerce.ProductServiceApp.Repository.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private ProductMapper productMapper;

    public ProductsService(CategoryRepository categoryRepository, ProductsRepository productsRepository, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.productsRepository = productsRepository;
        this.productMapper = productMapper;
    }

    public ResponseEntity<Product> createNewProduct(Product product,String token){

        Category category1= categoryRepository.findById(product.getCategoryId()).orElseThrow(()->new RuntimeException("Category with id doesnt exist"));

        return new ResponseEntity<>(productsRepository.save(product), HttpStatus.OK);
    }
    public ResponseEntity<List<Product>> getAllProducts(){

        return new ResponseEntity<>(productsRepository.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<Page<ProductsDto>> getAllProductsbyPage(Integer pageno, Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Product> productPage=productsRepository.findAll(pageable);
        Page<ProductsDto>productsDtoPage=productPage.map(entity->{
            ProductsDto productsDto=productMapper.converProductIntoDto(entity);
            return productsDto;
        });
        return new ResponseEntity<>(productsDtoPage, HttpStatus.OK);
    }
    public ResponseEntity<List<Product>> getAllProductsByCategoryId(String categoryId){
        return new ResponseEntity<>(productsRepository.findByCategoryId(categoryId), HttpStatus.OK);
    }
    public ResponseEntity<Product>getProductDetailById(String productId){
        return new ResponseEntity<>(productsRepository.findById(productId).orElseThrow(()->new RuntimeException("product with id not found")),HttpStatus.OK);
    }

    public String updateProductByid(String productId,Product product) {
        Product product1=productsRepository.findById(productId).orElseThrow(()->new RuntimeException("product with id not found"));
return  "product update sucess";
    }
    public String deleteProductByid(String productId) {
        productsRepository.findById(productId).orElseThrow(()->new RuntimeException("product with id not found"));
        productsRepository.deleteById(productId);
        return  "product delete sucess";
    }
}
