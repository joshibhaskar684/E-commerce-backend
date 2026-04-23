package app.auth.service.Controller.DashBoardController;


import app.auth.service.Service.MyUserServices;
import app.auth.service.Service.SellerService;
import app.auth.service.Service.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/dashboard")
public class MainPageController {
    private MyUserServices myUserServices;
    private SellerService sellerService;
    private ShopService shopService;

    public MainPageController(MyUserServices myUserServices, SellerService sellerService, ShopService shopService) {
        this.myUserServices = myUserServices;
        this.sellerService = sellerService;
        this.shopService = shopService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total/count")
    public ResponseEntity<List<Map<String ,String>>> totalShopsCount(){
        List<Map<String ,String>> totaldata=new ArrayList<>();
        totaldata.add(myUserServices.totalUsersCount());
        totaldata.add(sellerService.totalSellerCount());
        totaldata.add(shopService.totalShopCount());
        return new ResponseEntity<>(totaldata, HttpStatus.OK);
    }


}
