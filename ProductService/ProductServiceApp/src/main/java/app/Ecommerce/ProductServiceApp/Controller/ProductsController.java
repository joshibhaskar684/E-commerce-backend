package app.Ecommerce.ProductServiceApp.Controller;


import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Service.ProductsService;
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

    @PostMapping
    public ResponseEntity<Product>createNewProduct(@RequestBody Product product,@RequestBody String category ){
       return productsService.createNewProduct(product,category);
    }
    @GetMapping
    public ResponseEntity<List<Product>>getAllProducts(){
        return productsService.getAllProducts();
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Product>getProductDetailById(@PathVariable String productId){
        return productsService.getProductDetailById(productId);
    }
    @GetMapping("/cat")
    public ResponseEntity<List<Product>>getAllProductsById(@RequestParam String categoryid){
        return productsService.getAllProductsByCategoryId(categoryid);
    }
}
