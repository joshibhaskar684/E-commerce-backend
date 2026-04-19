package app.auth.service.Mapper;


import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.ShopDetailsDto;
import app.auth.service.DTO.ShopDto;
import app.auth.service.Entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

    public ShopDto convertToShopDto(Shop shop){
        ShopDto shopDto=new ShopDto(shop.getId(), shop.getShopName(), shop.getStatus(), shop.getCreatedAt(), shop.getUpdatedAt());
        return shopDto;
    }
    public ShopDetailsDto convertShopToShopDetailDTO(Shop shop){
        ShopDetailsDto shopDetailsDto=new ShopDetailsDto(shop.getId(),
                shop.getRejectionReason(),shop.getShopName(),
                shop.getDescription(),shop.getLogoUrl(),
                shop.getBannerUrl(),shop.getAddress(),
                shop.getCity(),shop.getState(),shop.getPincode(),
                shop.getStatus(),shop.getRating(),shop.getTotalReviews(),
                shop.getCreatedAt(),shop.getUpdatedAt(),shop.getSeller().getMobileNo(),
                shop.getSeller().getBusinessName(),shop.getSeller().getStatus()
                );
        return shopDetailsDto;
    }
}
