package app.auth.service.Service;

import app.auth.service.DTO.RejectRequest;
import app.auth.service.DTO.SellerDto;
import app.auth.service.DTO.SellerProfileDto;
import app.auth.service.Entity.Seller;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Enums.Status;
import app.auth.service.Exceptions.ResourceAlreadyExistsException;
import app.auth.service.Mapper.SellerMapper;
import app.auth.service.Repository.SellerRepository;
import app.auth.service.Repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class SellerService {

    private SellerRepository sellerRepository;
    private MyUserServices myUserServices;
    private SellerMapper sellerMapper;


    public SellerService(SellerRepository sellerRepository, MyUserServices myUserServices, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.myUserServices = myUserServices;
        this.sellerMapper = sellerMapper;
    }
    public SellerProfileDto getSellerProfile(String token) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.findByUser(userDetails).orElseThrow(()->new RuntimeException("seller not found "));
        return sellerMapper.convertInToSellerProfileDto(seller);
    }
    public Seller getSellerDataByid(Long id ){
        if(!sellerRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Seller Not Found");
        }
        return sellerRepository.getReferenceById(id);
    }

    public Map<String ,String> checkSellerApplication(String token) throws Exception {
        Map<String ,String> data=new HashMap<>();
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.findByUser(userDetails).get();
        if (seller.getStatus() == Status.PENDING) {
            data.put("Status",seller.getStatus().toString());
            data.put("Reason","It May Take 24hr-48hr.........");
        }
        else if(seller.getStatus()==Status.REJECTED){
            data.put("Status",seller.getStatus().toString());
            data.put("Reason",seller.getRejectionReason());
        }
        else{
            data.put("Status",seller.getStatus().toString());
        }
        return data;
    }

    public String CreateSeller(Seller seller, String token) throws Exception {
        UserDetailsEntity userDetails=myUserServices.findDetailsByJwt(token);
        if (sellerRepository.findByUser(userDetails).isPresent()) {
            throw new ResourceAlreadyExistsException("Seller already exists");
        }
        seller.setStatus(Status.PENDING);
        seller.setUser(userDetails);
        sellerRepository.save(seller);

        return "Seller Registered Sucessfully";
    }

    public Page<SellerDto> getUnapprovedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Seller> sellerPage=sellerRepository.findAllByStatus(Status.PENDING,pageable);
        Page<SellerDto> sellerDtos = sellerPage.map(entity -> sellerMapper.convertToSellerDTO(entity));

        return sellerDtos;
    }
    public Page<SellerDto> getApprovedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Seller> sellerPage=sellerRepository.findAllByStatus(Status.APPROVED,pageable);
        Page<SellerDto> sellerDtos = sellerPage.map(entity -> sellerMapper.convertToSellerDTO(entity));

        return sellerDtos;
    }
    public Page<SellerDto> getRejectedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Seller> sellerPage=sellerRepository.findAllByStatus(Status.REJECTED,pageable);
        Page<SellerDto> sellerDtos = sellerPage.map(entity -> sellerMapper.convertToSellerDTO(entity));

        return sellerDtos;
    }
    public Page<SellerDto> getSuspendedSellerList(Integer pageno,Integer pagesize){
        Pageable pageable= PageRequest.of(pageno,pagesize);
        Page<Seller> sellerPage=sellerRepository.findAllByStatus(Status.SUSPENDED,pageable);
        Page<SellerDto> sellerDtos = sellerPage.map(entity -> sellerMapper.convertToSellerDTO(entity));

        return sellerDtos;
    }

    public String rejectSeller(Long id, String token, RejectRequest rejectRequest) throws Exception {
        UserDetailsEntity admin=myUserServices.findDetailsByJwt(token);
        Seller seller=sellerRepository.getReferenceById(id);
        seller.setAdmin(admin);
        seller.setRejectionReason(rejectRequest.getReason());
        seller.setStatus(Status.REJECTED);
        sellerRepository.save(seller);
        return "Seller Rejected Sucessfully ";

    }

    public String approveSeller(Long id, String token) throws Exception {
        UserDetailsEntity admin=myUserServices.findDetailsByJwt(token);
       Seller seller=sellerRepository.getReferenceById(id);
       seller.setAdmin(admin);
       seller.setStatus(Status.APPROVED);
       myUserServices.changeRoleToSeller(seller.getUser().getEmail());
       sellerRepository.save(seller);
       return "Seller Approved Sucessfully ";

    }


}
