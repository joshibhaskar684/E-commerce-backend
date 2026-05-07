package app.auth.service.Service;

import app.auth.service.DTO.RejectRequest;
import app.auth.service.DTO.ShopDetailsDto;
import app.auth.service.DTO.ShopDto;
import app.auth.service.Entity.Seller;
import app.auth.service.Entity.Shop;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Mapper.ShopMapper;
import app.auth.service.Producers.KafkaEventProducer;
import app.auth.service.Producers.KafkaTopicProperties;
import app.auth.service.Repository.SellerRepository;
import app.auth.service.Repository.ShopRepository;
import com.ecommerce.commonlib.base_domains.Enums.EventType;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ChangeProductStatusWithShop;
import com.ecommerce.commonlib.base_domains.Event.ChangeShopStatusWithSeller;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
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
    private KafkaEventProducer kafkaEventProducer;
    private KafkaTopicProperties kafkaTopicProperties;

    public ShopService(SellerRepository sellerRepository, ShopRepository shopRepository, MyUserServices myUserServices, ShopMapper shopMapper, KafkaEventProducer kafkaEventProducer, KafkaTopicProperties kafkaTopicProperties) {
        this.sellerRepository = sellerRepository;
        this.shopRepository = shopRepository;
        this.myUserServices = myUserServices;
        this.shopMapper = shopMapper;
        this.kafkaEventProducer = kafkaEventProducer;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }




    public Map<String, String> totalShopCountForSeller(String token) throws Exception {

        Map<String, String> totalShops = new HashMap<>();

        totalShops.put("totalApprovedShop",
                String.valueOf(shopRepository.countByStatusAndSeller(Status.APPROVED,sellerRepository.findByUser(myUserServices.findDetailsByJwt(token)).orElseThrow(()->new RuntimeException("Seller data is Wrong ")))));

        totalShops.put("totalRejectedShop",
                String.valueOf(shopRepository.countByStatusAndSeller(Status.REJECTED,sellerRepository.findByUser(myUserServices.findDetailsByJwt(token)).orElseThrow(()->new RuntimeException("Seller data is Wrong ")))));


        totalShops.put("totalUnApprovedShop",
                String.valueOf(shopRepository.countByStatusAndSeller(Status.PENDING,sellerRepository.findByUser(myUserServices.findDetailsByJwt(token)).orElseThrow(()->new RuntimeException("Seller data is Wrong ")))));


        totalShops.put("totalSuspendedShop",
                String.valueOf(shopRepository.countByStatusAndSeller(Status.SUSPENDED,sellerRepository.findByUser(myUserServices.findDetailsByJwt(token)).orElseThrow(()->new RuntimeException("Seller data is Wrong ")))));


        totalShops.put("totalInActiveShop",
                String.valueOf(shopRepository.countByStatusAndSeller(Status.INACTIVE,sellerRepository.findByUser(myUserServices.findDetailsByJwt(token)).orElseThrow(()->new RuntimeException("Seller data is Wrong ")))));


        return totalShops;
    }


    public Map<String, String> totalShopCount() {

        Map<String, String> totalShops = new HashMap<>();

        totalShops.put("totalApprovedShop",
                String.valueOf(shopRepository.countByStatus(Status.APPROVED)));

        totalShops.put("totalRejectedShop",
                String.valueOf(shopRepository.countByStatus(Status.REJECTED)));

        totalShops.put("totalUnApprovedShop",
                String.valueOf(shopRepository.countByStatus(Status.PENDING)));

        totalShops.put("totalSuspendedShop",
                String.valueOf(shopRepository.countByStatus(Status.SUSPENDED)));

        totalShops.put("totalInActiveShop",
                String.valueOf(shopRepository.countByStatus(Status.INACTIVE)));

        return totalShops;
    }

    public String closeShop(String token, Long id) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Shop shop=shopRepository.getReferenceById(id);
        if(shop.getStatus().equals(Status.REJECTED)||shop.getStatus().equals(Status.SUSPENDED)||shop.getStatus().equals(Status.INACTIVE)){
            throw new RuntimeException("Operation denied ! Shop is already Suspended/Rejected ");

        }
        if(!userDetails.equals(shop.getSeller().getUser())){
            throw new RuntimeException("Access denied ! wrong User / Owner");
        }
        shop.setStatus(Status.INACTIVE);
        Shop shopforEvent=shopRepository.save(shop);
        if(shop.getStatus().equals(Status.APPROVED)){
        ShopApprovedEvent event = new ShopApprovedEvent();
        event.setShopId(shopforEvent.getId());
        event.setSellerId(shopforEvent.getSeller().getId());
        event.setStatus(Status.INACTIVE);
        event.setEventType(EventType.INACTIVE);
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getShop(),event);
        }
        return "Shop Close Sucessfully";
    }


    public Page<ShopDto> getUnapprovedSellerList(Integer pageno, Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findByStatusAndSeller_Status(Status.PENDING,Status.APPROVED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));
        return sellerDtos;
    }
    public Page<ShopDto> getApprovedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findByStatusAndSeller_Status(Status.APPROVED,Status.APPROVED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));

        return sellerDtos;
    }
    public Page<ShopDto> getRejectedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findByStatusAndSeller_Status(Status.REJECTED,Status.APPROVED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));
        return sellerDtos;
    }
    public Page<ShopDto> getSuspendedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Shop> shopsPage=shopRepository.findByStatusAndSeller_Status(Status.SUSPENDED,Status.APPROVED,pageable);
        Page<ShopDto> sellerDtos = shopsPage.map(entity -> shopMapper.convertToShopDto(entity));

        return sellerDtos;
    }



    public String createShop (String token , Shop shop) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);

        Seller seller=sellerRepository.findByUser(userDetails).orElseThrow(()->new RuntimeException("Seller Not Found"));
        if(seller.getStatus() != Status.APPROVED){
            throw new RuntimeException("Seller not approved");
        }
       List< Shop> shop1=shopRepository.findBySeller(seller);
        if(!shop1.isEmpty()){
            throw new RuntimeException("Shop Already Exists");
        }
        shop.setStatus(Status.PENDING);
        shop.setSeller(seller);
        shopRepository.save(shop);
        return "Shop Created Sucessfully";
    }
    public String applyForShop (String token , Shop shop) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.findByUser(userDetails).orElseThrow(() -> new RuntimeException("Seller not found"));;
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
        Shop shopforEvent=shopRepository.save(shop);

        ShopApprovedEvent event = new ShopApprovedEvent();
        event.setSellerId(shopforEvent.getSeller().getId());
        event.setStatus(Status.APPROVED);
        event.setShopId(shopforEvent.getId());
        event.setEventType(EventType.APPROVE);
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getShop(),event);

//        ChangeProductStatusWithShop event2 = new ChangeProductStatusWithShop(shop.getId(),"Change Product Status with Shop",EventType.APPROVE);
//        kafkaEventProducer.sendEvent(kafkaTopicProperties.getShop(),event2);


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


    public ShopDetailsDto getShopDataByid(Long id,String token) throws Exception {
        if(!shopRepository.existsById(id)){
            throw new RuntimeException("Shop Not Found");
        }
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);

        if(!shopRepository.getReferenceById(id).getSeller().getUser().equals(userDetails)){
            if(userDetails.getRole().equals("ADMIN")){
                return shopMapper.convertShopToShopDetailDTO(shopRepository.getReferenceById(id));
            }
            throw new RuntimeException("Seller Not Found/Mismatch");
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
        Shop shopforEvent=shopRepository.save(shop);
        ShopApprovedEvent event = new ShopApprovedEvent();
        event.setSellerId( shopforEvent.getSeller().getId());
        event.setStatus(Status.SUSPENDED);
        event.setEventType(EventType.SUSPEND);
        event.setShopId(shopforEvent.getId());
        kafkaEventProducer.sendEvent(kafkaTopicProperties.getShop(),event);


        return "Shop Suspended Sucessfully";
    }

    public Map<String, Object> getShopIdListAndSellerId(String token) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Map<String, Object> data=new HashMap<>();
        Seller sellerData=sellerRepository.findByUser(userDetails).get();
        data.put("sellerId",sellerData.getId());
        data.put("shopIdData",shopMapper.convertListShopToListShopDto(shopRepository.findBySellerAndStatus(sellerData,Status.APPROVED)));
        return data;

    }
}
