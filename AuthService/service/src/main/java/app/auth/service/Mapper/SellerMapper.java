package app.auth.service.Mapper;

import app.auth.service.DTO.SellerDto;
import app.auth.service.Entity.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {
    public SellerDto convertToSellerDTO(Seller seller){
        SellerDto sellerDto=new SellerDto();
        sellerDto.setId(seller.getId());
        sellerDto.setBusinessName(seller.getBusinessName());
        sellerDto.setStatus(seller.getStatus());
        sellerDto.setCreatedAt(seller.getCreatedAt());
        sellerDto.setUpdatedAt(seller.getUpdatedAt());
        return sellerDto;
    }
}
