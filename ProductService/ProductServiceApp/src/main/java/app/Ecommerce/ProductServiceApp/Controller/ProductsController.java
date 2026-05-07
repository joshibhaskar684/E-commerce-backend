package app.Ecommerce.ProductServiceApp.Controller;


import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Service.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }



    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<String>createNewProduct(){
        return new ResponseEntity<>("done ok ", HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Product>createNewProduct(@RequestBody Product product, @RequestHeader("Authorization") String authHeader){
        String token=authHeader.substring(7);
       return productsService.createNewProduct(product,token);
    }
    @GetMapping
    public ResponseEntity<List<Product>>getAllProducts(){
        return productsService.getAllProducts();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductsDto>>getAllProductsPage(@RequestParam Integer pageno, @RequestParam Integer pagesize){

        return productsService.getAllProductsbyPage(pageno,pagesize);
    }

    @GetMapping("/page/seller")
    public ResponseEntity<Page<ProductsDto>>getAllProductsPageForSeller(@RequestHeader("Authorization") String authHeader,@RequestParam Integer pageno, @RequestParam Integer pagesize){
        String token=authHeader.substring(7);


        return productsService.getAllProductsbyPageForSeller(token,pageno,pagesize);
    }

    @GetMapping("/page/category/main")
    public ResponseEntity<Page<ProductsDto>>getAllProductWhereCategory(@RequestParam String category,@RequestParam Integer pageno, @RequestParam Integer pagesize){
        return productsService.getAllCategoryProductbyPage(category,pageno,pagesize);
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<String>updateProductBYId(@PathVariable String productId,@RequestBody Product product){

        return new ResponseEntity<>(productsService.updateProductByid(productId,product),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String>deleteProductBYId(@PathVariable String productId){

        return new ResponseEntity<>(productsService.deleteProductByid(productId),HttpStatus.OK);
    }


    @GetMapping("/seller/{productId}")
    public ResponseEntity<Product>getProductDetailByIdForSeller(@RequestHeader("Authorization") String authHeader,@PathVariable String productId){
       String token=authHeader.substring(7);

        return productsService.getProductDetailByIdForSeller(token,productId);
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
