package app.Ecommerce.ProductServiceApp.ElasticSearch.Controller;

import app.Ecommerce.ProductServiceApp.ElasticSearch.Entity.ProductDocument;
import app.Ecommerce.ProductServiceApp.ElasticSearch.Service.ProductSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ElasticController {


    private ProductSearchService productSearchService;

    public ElasticController(ProductSearchService productSearchService){
        this.productSearchService=productSearchService;
    }



    @GetMapping("/suggestions")
    public List<ProductDocument> suggestions(
            @RequestParam String q) {

        return productSearchService.suggestions(q);
    }
}
