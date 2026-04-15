package app.auth.service.Mapper;


import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.ShopDto;
import app.auth.service.Entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

    public ShopDto convertToShopDto(Shop shop){
        ShopDto shopDto=new ShopDto(shop.getId(), shop.getShopName(), shop.getStatus(), shop.getCreatedAt(), shop.getUpdatedAt());
        return shopDto;
    }
}
