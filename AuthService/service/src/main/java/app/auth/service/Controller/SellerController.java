package app.auth.service.Controller;


import app.auth.service.DTO.RejectRequest;
import app.auth.service.DTO.SellerDto;
import app.auth.service.Entity.Seller;
import app.auth.service.Service.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SellerController {
    private SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }


    @GetMapping("/seller/check/seller/{id}")
    public ResponseEntity<Seller> findSellerById(@PathVariable Long id){
        return new ResponseEntity<>( sellerService.getSellerDataByid(id),HttpStatus.OK);
    }

    @PostMapping("/seller/create/seller")
    public ResponseEntity<String> registerSeller(@RequestBody Seller seller,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(sellerService.CreateSeller(seller,token), HttpStatus.OK);
    }



    @PostMapping("/seller/approve/seller/{id}")
    public ResponseEntity<String> approveSeller(@PathVariable Long id,@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(sellerService.approveSeller(id,token), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/seller/unapprove/seller/page")
    public ResponseEntity <Page<SellerDto>> unapproveSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(sellerService.getUnapprovedSellerList(pageno,pagesize),HttpStatus.OK);

    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/seller/approve/seller/page")
    public ResponseEntity <Page<SellerDto>> approveSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(sellerService.getApprovedSellerList(pageno,pagesize),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/seller/suspended/seller/page")
    public ResponseEntity <Page<SellerDto>> suspendedSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(sellerService.getSuspendedSellerList(pageno,pagesize),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/seller/rejected/seller/page")
    public ResponseEntity <Page<SellerDto>> rejectedSellerList(@RequestParam Integer pageno,@RequestParam Integer pagesize){
        return new ResponseEntity<>(sellerService.getRejectedSellerList(pageno,pagesize),HttpStatus.OK);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/seller/reject/seller/{id}")
    public ResponseEntity<String> rejectSeller(@PathVariable Long id, @RequestHeader("Authorization") String authHeader, @RequestBody RejectRequest rejectRequest) throws Exception {
        String token=authHeader.substring(7);
        return new ResponseEntity<>(sellerService.rejectSeller(id,token,rejectRequest), HttpStatus.OK);
    }
}
