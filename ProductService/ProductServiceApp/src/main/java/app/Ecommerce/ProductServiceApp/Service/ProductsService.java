package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.Entity.Category;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import app.Ecommerce.ProductServiceApp.Mapper.ProductMapper;
import app.Ecommerce.ProductServiceApp.Producers.KafkaEventProducer;
import app.Ecommerce.ProductServiceApp.Producers.KafkaTopicProperties;
import app.Ecommerce.ProductServiceApp.Repository.CategoryRepository;
import app.Ecommerce.ProductServiceApp.Repository.ProductsRepository;
import app.Ecommerce.ProductServiceApp.Repository.SellerDataRepository;
import app.Ecommerce.ProductServiceApp.Security.JwtUtil;
import com.ecommerce.commonlib.base_domains.Enums.EventType;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ProductsEvent;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.connection.tlschannel.NeedsWriteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductsService {

    private  CategoryRepository categoryRepository;
    private ProductsRepository productsRepository;
    private ProductMapper productMapper;
    private MongoTemplate mongoTemplate;
    private JwtUtil jwtUtil;
    private SellerDataRepository sellerDataRepository;
    private KafkaEventProducer kafkaEventProducer;
    private KafkaTopicProperties kafkaTopicProperties;

    public ProductsService(CategoryRepository categoryRepository, ProductsRepository productsRepository, ProductMapper productMapper, MongoTemplate mongoTemplate, JwtUtil jwtUtil, SellerDataRepository sellerDataRepository, KafkaEventProducer kafkaEventProducer, KafkaTopicProperties kafkaTopicProperties) {
        this.categoryRepository = categoryRepository;
        this.productsRepository = productsRepository;
        this.productMapper = productMapper;
        this.mongoTemplate = mongoTemplate;
        this.jwtUtil = jwtUtil;
        this.sellerDataRepository = sellerDataRepository;
        this.kafkaEventProducer = kafkaEventProducer;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }
    public ResponseEntity<String>updateProductStatusById(String id,Long sellerId, Status status,Status changeStatus){
        if(changeStatus.equals(Status.ACTIVE)){
            throw new RuntimeException("Status Cant Be UPDATED TO aCTIVE ");
        }
        Product product=productsRepository.findByIdAndSellerIdAndProductStatus(id,sellerId,status).orElseThrow(()->new RuntimeException("Product with id not found/ seller is not available"));
        product.setProductStatus(changeStatus);
        productsRepository.save(product);
        ProductsEvent productsEvent=new ProductsEvent();
        productsEvent.setProductId(product.getId());
        productsEvent.setProductStatus(product.getProductStatus());
        productsEvent.setSellerId(product.getSellerId());
        productsEvent.setShopId(product.getShopId());
        productsEvent.setUpdateAt(product.getUpdatedAt());
        productsEvent.setEventType(EventType.UPDATE);
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getProducts(),productsEvent);

        return new ResponseEntity<>("Poduct Updated sucessfully" ,HttpStatus.OK);
    }
    public ResponseEntity <String>updateQuantityByProductAndSellerId(String productId,Long sellerId,Integer quantity){
        if(quantity<1){
            throw new RuntimeException("Quantity is invaild ");
        }
        Product product=productsRepository.findByIdAndSellerIdAndProductStatus(productId,sellerId,Status.ACTIVE).orElseThrow(()->new RuntimeException("Given data were wrong "));
        product.setQuantity(quantity);
        productsRepository.save(product);
        ProductsEvent productsEvent=new ProductsEvent();
        productsEvent.setProductId(product.getId());
        productsEvent.setQuantity(product.getQuantity());
        productsEvent.setShopId(product.getShopId());
        productsEvent.setSellerId(product.getSellerId());
        productsEvent.setProductStatus(product.getProductStatus());
        productsEvent.setUpdateAt(product.getUpdatedAt());
        productsEvent.setEventType(EventType.UPDATE);
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getProducts(),productsEvent);
        return new ResponseEntity<>("Quantity Update Sucessfully ",HttpStatus.OK);
    }

    public ResponseEntity<Page<ProductsDto>> getAllCategoryProductbyPage(String category, Integer pageno, Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Product> productPage=productsRepository.findByCategoryPathContaining(category,pageable);
        Page<ProductsDto>productsDtoPage=productPage.map(entity->{
            ProductsDto productsDto=productMapper.converProductIntoDto(entity);
            return productsDto;
        });
        return new ResponseEntity<>(productsDtoPage, HttpStatus.OK);
    }
    public ResponseEntity<Page<ProductsDto>> getAllQueryProductbyPage(String query,Integer pageno, Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Product> productPage=productsRepository.findByNameContainingIgnoreCase(query,pageable);
        Page<ProductsDto>productsDtoPage=productPage.map(entity->{
            ProductsDto productsDto=productMapper.converProductIntoDto(entity);
            return productsDto;
        });
        return new ResponseEntity<>(productsDtoPage, HttpStatus.OK);
    }
    public ResponseEntity<Product> createNewProduct(Product product, String token){
        Long sellerId=jwtUtil.extractSellerId(token);
        if(!sellerId.equals(product.getSellerId()) && product.getQuantity()<1 && categoryRepository.findById(product.getCategoryId()).isEmpty() && sellerDataRepository.findBySellerIdAndShopIdAndStatus(product.getSellerId(),product.getShopId(),Status.APPROVED).isEmpty()){
            throw new RuntimeException("Seller id Mismatch / Category With Id Not found / Product quantity is less than 1 / seller & Shop Don't have any permission ");
        }
        product.setProductStatus(Status.ACTIVE);

        Product productdata=productsRepository.save(product);
        ProductsEvent productsEvent=new ProductsEvent();
        productsEvent.setProductId(productdata.getId());
        productsEvent.setQuantity(productdata.getQuantity());
        productsEvent.setShopId(productdata.getShopId());
        productsEvent.setSellerId(productdata.getSellerId());
        productsEvent.setProductStatus(productdata.getProductStatus());
        productsEvent.setUpdateAt(productdata.getUpdatedAt());
        productsEvent.setEventType(EventType.CREATE);
        productsEvent.setImages(productdata.getImages());
        productsEvent.setName(productdata.getName());
        productsEvent.setColor(productdata.getColor());
        productsEvent.setPrice(productdata.getPrice());
        productsEvent.setOriginalPrice(productdata.getOriginalPrice());
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getProducts(),productsEvent);
        return new ResponseEntity<>(productdata, HttpStatus.OK);
    }
    public ResponseEntity<Page<ProductsDto>> getAllProductsbyPageForSeller(String token,Integer pageno, Integer pagesize){

        Long sellerId=jwtUtil.extractSellerId(token);
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Product> productPage=productsRepository.findAllBySellerIdAndProductStatus(sellerId,Status.ACTIVE,pageable);
        Page<ProductsDto>productsDtoPage=productPage.map(entity->{
            ProductsDto productsDto=productMapper.converProductIntoDto(entity);
            return productsDto;
        });
        return new ResponseEntity<>(productsDtoPage, HttpStatus.OK);
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

    public long updateAllProductsBySeller(Long sellerId, Status oldStatus, Status newStatus) {

        Query query = new Query();
        query.addCriteria(Criteria.where("sellerId").is(sellerId)
                .and("productStatus").is(oldStatus));

        Update update = new Update();
        update.set("productStatus", newStatus);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Product.class);

        return result.getModifiedCount();
    }


    public static Double calculateDiscount(BigDecimal price, BigDecimal originalPrice) {
        if (price == null || originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return originalPrice.subtract(price)
                .divide(originalPrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }


    public static Boolean isInStock(Integer quantity) {
        return quantity != null && quantity > 0;
    }

    public ResponseEntity<Product> getProductDetailByIdForSeller(String token, String productId) {

        Long sellerId=jwtUtil.extractSellerId(token);
        Product product=productsRepository.findByIdAndSellerIdAndProductStatus(productId,sellerId,Status.ACTIVE).get();
        return new ResponseEntity<>(product,HttpStatus.OK);
    }
}
