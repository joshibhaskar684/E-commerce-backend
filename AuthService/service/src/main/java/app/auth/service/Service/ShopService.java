package app.auth.service.Service;

import app.auth.service.DTO.RejectRequest;
import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.ShopDetailsDto;
import app.auth.service.DTO.ShopDto;
import app.auth.service.Entity.Seller;
import app.auth.service.Entity.Shop;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Enums.Status;
import app.auth.service.Mapper.ShopMapper;
import app.auth.service.Repository.SellerRepository;
import app.auth.service.Repository.ShopRepository;
import app.auth.service.Repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopService {
    private SellerRepository sellerRepository;
    private ShopRepository shopRepository;
    private MyUserServices myUserServices;
    private ShopMapper shopMapper;

    public ShopService(SellerRepository sellerRepository, ShopRepository shopRepository, MyUserServices myUserServices, ShopMapper shopMapper) {
        this.sellerRepository = sellerRepository;
        this.shopRepository = shopRepository;
        this.myUserServices = myUserServices;
        this.shopMapper = shopMapper;
    }



    public Page<ShopDto> getUnapprovedSellerList(Integer pageno, Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findAllByStatus(Status.PENDING,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));
        return sellerDtos;
    }
    public Page<ShopDto> getApprovedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findAllByStatus(Status.APPROVED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));

        return sellerDtos;
    }
    public Page<ShopDto> getRejectedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findAllByStatus(Status.REJECTED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));
        return sellerDtos;
    }
    public Page<ShopDto> getSuspendedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findAllByStatus(Status.SUSPENDED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));

        return sellerDtos;
    }



    public String createShop (String token , Shop shop) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);

        Seller seller=sellerRepository.findByUser(userDetails).orElseThrow(()->new RuntimeException("Seller Not Found"));
       List< Shop> shop1=shopRepository.findBySeller(seller);
        if(shop1.size()>1){
            throw new RuntimeException("Shop Already Exist");
        }
        shop.setStatus(Status.PENDING);
        shop.setSeller(seller);
        shopRepository.save(shop);
        return "Shop Created Sucessfully";
    }
    public String applyForShop (String token , Shop shop) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.findByUser(userDetails).get();
        shop.setStatus(Status.PENDING);
        shop.setSeller(seller);
        shopRepository.save(shop);
        return "Application has been Submitted Sucessfully";
    }

    public List<Map<String, String>> getMyShop(String token) throws Exception {

        UserDetailsEntity userDetails = myUserServices.findDetailsByJwt(token);

        Seller seller = sellerRepository.findByUser(userDetails)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Shop> shops = shopRepository.findBySeller(seller);

        return shops.stream().map(entity -> {
            Map<String, String> dat = new HashMap<>();
            dat.put("id", entity.getId().toString());
            dat.put("Name", entity.getShopName());
            dat.put("Status", entity.getStatus().toString());
            return dat;
        }).toList();
    }

    public  String approveShop(String token,Long id) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Shop shop=shopRepository.getReferenceById(id);
        shop.setStatus(Status.APPROVED);
        shop.setAdmin(userDetails);
        shopRepository.save(shop);
        return "Shop Approved Sucessfully";
    }


    public String rejectShop(String token, Long id, RejectRequest rejectRequest) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Shop shop=shopRepository.getReferenceById(id);
        shop.setStatus(Status.REJECTED);
        shop.setAdmin(userDetails);
        shop.setRejectionReason(rejectRequest.getReason());
        shopRepository.save(shop);
        return "Shop Rejected Sucessfully";
    }


    public ShopDetailsDto getShopDataByid(Long id) {
        if(!shopRepository.existsById(id)){
            throw new RuntimeException("Shop Not Found");
        }
        return shopMapper.convertShopToShopDetailDTO(shopRepository.getReferenceById(id));
    }
    public List<ShopDto> getShopListBasic(String token) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.findByUser(userDetails).get();
        List<Shop> shops=shopRepository.findBySeller(seller);
        return shops.stream().map(entity -> shopMapper.convertToShopDto(entity)).toList();
    }

    public String suspendShop(String token, Long id, RejectRequest rejectRequest) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Shop shop=shopRepository.getReferenceById(id);
        shop.setStatus(Status.SUSPENDED);
        shop.setAdmin(userDetails);
        shop.setRejectionReason(rejectRequest.getReason());
        shopRepository.save(shop);
        return "Shop Suspended Sucessfully";
    }

}
