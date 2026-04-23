package app.auth.service.Controller;


import app.auth.service.DTO.RejectRequest;
import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.ShopDetailsDto;
import app.auth.service.DTO.ShopDto;
import app.auth.service.Entity.Seller;
import app.auth.service.Entity.Shop;
import app.auth.service.Service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/shops")
public class ShopController {
    private ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/shop/count")
    public ResponseEntity<Map<String ,String>> totalShopsCount(){

        return new ResponseEntity<>(shopService.totalShopCount(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller/shop/count")
    public ResponseEntity<Map<String ,String>> totalShopsCountForSeller(@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);

        return new ResponseEntity<>(shopService.totalShopCountForSeller(token),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/close/{id}")
    public ResponseEntity<String> CloseShopWithId(@PathVariable Long id,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.closeShop(token,id), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/apply/total/shop")
    public ResponseEntity<List<ShopDto>> getBasicListOfShop(@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.getShopListBasic(token), HttpStatus.OK);
    }

    @GetMapping("/myshop")
    public ResponseEntity<Object> getMyShop(@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.getMyShop(token), HttpStatus.OK);
    }

    @PostMapping("/applyforshop")
    public ResponseEntity<String> applyForShop(@RequestBody Shop shop,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.applyForShop(token,shop), HttpStatus.OK);
    }


    @GetMapping("/check/shop/{id}")
    public ResponseEntity<ShopDetailsDto> findShopById(@PathVariable Long id,@RequestHeader("Authorization") String authHeader) throws Exception {

        String token=authHeader.substring(7);
        return new ResponseEntity<>( shopService.getShopDataByid(id,token), HttpStatus.OK);
    }

    @PostMapping("/create/shop")
    public ResponseEntity<String> registerShop(@RequestBody Shop shop,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.createShop(token,shop), HttpStatus.OK);
    }



    @PostMapping("/approve/shop/{id}")
    public ResponseEntity<String> approveSeller(@PathVariable Long id,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.approveShop(token,id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unapprove/shop/page")
    public ResponseEntity <Page<ShopDto>> unapproveSellerList(@RequestParam Integer pageno, @RequestParam Integer pagesize){
        return new ResponseEntity<>(shopService.getUnapprovedSellerList(pageno,pagesize),HttpStatus.OK);

    }

    //    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approve/shop/page")
    public ResponseEntity <Page<ShopDto>> approveSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(shopService.getApprovedSellerList(pageno,pagesize),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/suspended/shop/page")
    public ResponseEntity <Page<ShopDto>> suspendedSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(shopService.getSuspendedSellerList(pageno,pagesize),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rejected/shop/page")
    public ResponseEntity <Page<ShopDto>> rejectedSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(shopService.getRejectedSellerList(pageno,pagesize),HttpStatus.OK);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/shop/{id}")
    public ResponseEntity<String> rejectSeller(@PathVariable Long id, @RequestHeader("Authorization") String authHeader, @RequestBody RejectRequest rejectRequest) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.rejectShop(token,id,rejectRequest), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/suspend/shop/{id}")
    public ResponseEntity<String> suspendSeller(@PathVariable Long id, @RequestHeader("Authorization") String authHeader, @RequestBody RejectRequest rejectRequest) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(shopService.suspendShop(token,id,rejectRequest), HttpStatus.OK);
    }

}
