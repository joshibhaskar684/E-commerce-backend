package app.Ecommerce.ProductServiceApp.Controller;


import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Service.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/test")
    public ResponseEntity<String>createNewProduct(){
        return new ResponseEntity<>("done ok ", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product>createNewProduct(@RequestBody Product product ){
       return productsService.createNewProduct(product);
    }
    @GetMapping
    public ResponseEntity<List<Product>>getAllProducts(){
        return productsService.getAllProducts();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductsDto>>getAllProductsPage(@RequestParam Integer pageno, @RequestParam Integer pagesize){

        return productsService.getAllProductsbyPage(pageno,pagesize);
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<String>updateProductBYId(@PathVariable String productId,@RequestBody Product product){

        return new ResponseEntity<>(productsService.updateProductByid(productId,product),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String>deleteProductBYId(@PathVariable String productId){

        return new ResponseEntity<>(productsService.deleteProductByid(productId),HttpStatus.OK);
    }



    @GetMapping("/{productId}")
    public ResponseEntity<Product>getProductDetailById(@PathVariable String productId){
        return productsService.getProductDetailById(productId);
    }
    @GetMapping("/categoryId/products")
    public ResponseEntity<List<Product>>getAllProductsByCategoryId(@RequestParam String categoryId){
        return productsService.getAllProductsByCategoryId(categoryId);
    }
}
