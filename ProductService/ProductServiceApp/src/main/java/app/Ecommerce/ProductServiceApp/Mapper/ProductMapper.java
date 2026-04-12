package app.Ecommerce.ProductServiceApp.Mapper;

import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;


@Data
@Component
public class ProductMapper {

    public ProductsDto converProductIntoDto(Product product){
        ProductsDto productsDto=new ProductsDto();
        productsDto.setId(product.getId());
        productsDto.setBrand(product.getBrand());
       productsDto.setName(product.getName());
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            productsDto.setImage(product.getImages().get(product.getImages().size() - 1));
        }
        productsDto.setPrice(product.getPrice());
        productsDto.setOriginalPrice(product.getOriginalPrice());
        productsDto.setStoreId(product.getStoreId());
        productsDto.setCategoryPath(product.getCategoryPath());
        productsDto.setCreatedAt(product.getCreatedAt());
        productsDto.setUpdatedAt(product.getUpdatedAt());
        return productsDto;
    }


}
