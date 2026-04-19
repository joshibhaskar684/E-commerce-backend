package app.auth.service.Mapper;

import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.SellerProfileDto;
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
    public SellerProfileDto convertInToSellerProfileDto(Seller seller){
        SellerProfileDto sellerProfileDto=new SellerProfileDto(seller.getId(),
                seller.getBusinessName(),seller.getUser().getEmail(),
                seller.getMobileNo(),seller.getStatus(),seller.getCreatedAt(),seller.getAddress(),seller.getCity(),
                seller.getState(),seller.getPincode());
        return sellerProfileDto;
    }
}
