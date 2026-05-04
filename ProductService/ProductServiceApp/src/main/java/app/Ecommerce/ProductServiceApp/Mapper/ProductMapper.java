package app.Ecommerce.ProductServiceApp.Mapper;

import app.Ecommerce.ProductServiceApp.DTO.ProductDetailsDto;
import app.Ecommerce.ProductServiceApp.DTO.ProductsDto;
import app.Ecommerce.ProductServiceApp.DTO.VariantDto;
import app.Ecommerce.ProductServiceApp.Entity.Product;
import jakarta.ws.rs.core.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static app.Ecommerce.ProductServiceApp.Service.ProductsService.calculateDiscount;
import static app.Ecommerce.ProductServiceApp.Service.ProductsService.isInStock;


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
        productsDto.setCategoryPath(product.getCategoryPath());
        productsDto.setCreatedAt(product.getCreatedAt());
        productsDto.setUpdatedAt(product.getUpdatedAt());
        return productsDto;
    }
    public ProductDetailsDto mapToDto(Product product) {

        return ProductDetailsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .slug(product.getSlug())
                .categoryId(product.getCategoryId())
                .categoryPath(product.getCategoryPath())
                .description(product.getDescription())
                .images(product.getImages())
                .color(product.getColor())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .discountPercentage(
                        calculateDiscount(product.getPrice(), product.getOriginalPrice())
                )
                .quantity(product.getQuantity())
                .inStock(isInStock(product.getQuantity()))
                .returnDay(product.getReturnDay())
                .averageRating(product.getAverageRating())
                .totalReviews(product.getTotalReviews())
                .freeShipping(product.getFreeShipping())
                .deliveryDays(product.getDeliveryDays())
                .shopId(product.getShopId())
                .sellerId(product.getSellerId())
                .productStatus(product.getProductStatus())
//                .variants(mapVariants(product.getVariants()))
                .specifications(product.getSpecifications())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
//
//    private List<VariantDto> mapVariants(List<Variant> variants) {
//
//        if (variants == null || variants.isEmpty()) {
//            return Collections.emptyList();
//        }

//        return variants.stream()
//                .map(variant -> VariantDto.builder()
//                        .name(variant.getName())
//                        .value(variant.getValue())
//                        .price(variant.getPrice())
//                        .quantity(variant.getQuantity())
//                        .inStock(variant.getQuantity() != null && variant.getQuantity() > 0)
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }

//    public static VariantDto from(Variant variant) {
//        return VariantDto.builder()
//                .name(variant.getName())
//                .value(variant.getValue())
//                .price(variant.getPrice())
//                .quantity(variant.getQuantity())
//                .inStock(variant.getQuantity() != null && variant.getQuantity() > 0)
//                .build();
//    }


}
