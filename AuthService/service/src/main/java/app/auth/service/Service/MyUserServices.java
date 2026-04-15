package app.auth.service.Service;

import app.auth.service.DTO.AdminDTO;
import app.auth.service.DTO.ResponseDto;
import app.auth.service.DTO.Signupdto;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Repository.UsersRepository;
import app.auth.service.Security.JwtUtil;
import app.auth.service.Security.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class MyUserServices implements UserDetailsService {
    private UsersRepository usersRepository;
    private JwtUtil jwtUtil;
    private EmailServices emailServices;

    public MyUserServices(UsersRepository usersRepository, JwtUtil jwtUtil, EmailServices emailServices) {
        this.usersRepository = usersRepository;
        this.jwtUtil = jwtUtil;
        this.emailServices = emailServices;
    }

    public String generateOtp() {
        int otp = 1000 + new Random().nextInt(9000); // 4-digit OTP
        return String.valueOf(otp);
    }

    public ResponseEntity<Map<String,String>> findUserByJwtMap(String token) throws Exception{
        String email=jwtUtil.extractUsername(token);
        Optional<UserDetailsEntity> userDetails=usersRepository.findByEmail(email);
        Map<String,String>userdata=new HashMap<>();
        if(userDetails.isEmpty()){
            return new ResponseEntity<>(userdata, HttpStatus.NOT_FOUND);
        }
        userdata.put("Name",userDetails.get().getName());
        userdata.put("Email",userDetails.get().getEmail());

        userdata.put("Mobile",userDetails.get().getMobileno());

        userdata.put("SocialLogin",userDetails.get().getProvider());

        return new ResponseEntity<>(userdata,HttpStatus.OK);
    }
    public String changeRoleToSeller(String email){
        UserDetailsEntity seller=usersRepository.findByEmail(email).get();
        if (seller.getRole().equals("ADMIN")){
            return "Sucess";
        }
        seller.setRole("SELLER");
        usersRepository.save(seller);
        return "Role Changed to Seller  Sucess";

    }
//
    public ResponseEntity<Signupdto>findUserByJwt(String token) throws Exception{
        String email=jwtUtil.extractUsername(token);
        Optional<UserDetailsEntity> userDetails=usersRepository.findByEmail(email);
        if(userDetails.isEmpty()){
            throw new Exception("User Not Found With Email"+email);
        }
        Signupdto user=new Signupdto();
        user.setEmail(userDetails.get().getEmail());
        user.setMobileno(userDetails.get().getMobileno());
        user.setName(userDetails.get().getName());
        user.setPassword(userDetails.get().getPassword());

        return new ResponseEntity<Signupdto>(user,HttpStatus.OK);
    }

    public UserDetailsEntity findUserByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user with email not found"));
    }

    public AdminDTO findAdminByJwt(String token) throws Exception{
        String email=jwtUtil.extractUsername(token);
        Optional<UserDetailsEntity> userDetails=usersRepository.findByEmail(email);
        if(userDetails.isEmpty()){
            throw new Exception("Admin Not Found With Email"+email);
        }
        UserDetailsEntity saveuser=userDetails.get();
        String Otp=generateOtp();
       saveuser.setOtp(Otp);
       usersRepository.save(saveuser);
        emailServices.sendOtpEmail(userDetails.get().getEmail(),Otp);
        AdminDTO user=new AdminDTO();
        user.setEmail(userDetails.get().getEmail());
        user.setMobileno(userDetails.get().getMobileno());
        user.setName(userDetails.get().getName());
        user.setPassword(userDetails.get().getPassword());
        user.setRole(userDetails.get().getRole());

        return user;
    }
    public UserDetailsEntity findDetailsByJwt(String token) throws Exception{
        String email=jwtUtil.extractUsername(token);
        Optional<UserDetailsEntity> userDetails=usersRepository.findByEmail(email);
        if(userDetails.isEmpty()){
            throw new Exception("Admin Not Found With Email"+email);
        }

        UserDetailsEntity user= userDetails.get();
            return user;
    }
    @Transactional
    public ResponseEntity<ResponseDto> createUser(Signupdto signupdto) {

        // 1. Always check email first
        if (usersRepository.findByEmail(signupdto.getEmail()).isPresent()) {
            return new ResponseEntity<>(
                    new ResponseDto("", "User already registered!"),
                    HttpStatus.CONFLICT
            );
        }

        UserDetailsEntity user = new UserDetailsEntity();
        user.setEmail(signupdto.getEmail());
        user.setName(signupdto.getName());
        user.setPassword(
                new BCryptPasswordEncoder(12).encode(signupdto.getPassword())
        );
        user.setMobileno(signupdto.getMobileno());

        // 2. SECURE role assignment
        boolean adminExists = usersRepository.existsByRole("ADMIN");

        if (!adminExists) {
            // FIRST USER ONLY
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        usersRepository.save(user);

        return new ResponseEntity<>(
                new ResponseDto("",
                        user.getRole().equals("ADMIN")
                                ? "Admin Registered Successfully!"
                                : "User Registered Successfully!"
                ),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<String> VERIFYADMINBYOTP(String token, String otp, HttpServletResponse response) throws Exception {

        UserDetailsEntity user= findDetailsByJwt(token);
          if (!"ADMIN".equals(user.getRole()) || !otp.equals(user.getOtp())) {
            throw new Exception("Invalid token / OTP");
        }

        List<String> roles=new ArrayList<>();
          roles.add("ADMIN");
        String verifiedJwt = jwtUtil.generateAdminToken(user.getEmail(), roles,true);

        ResponseCookie cookie = ResponseCookie.from("adminSession", verifiedJwt)
                .httpOnly(true)                 // prevent JS access
                .secure(true)                   // HTTPS only
                .path("/")                      // send to all backend paths
                .sameSite("None")               // allow cross-domain
                .domain(".vhbuyio.site")        // allow subdomain access
                .maxAge(Duration.ofHours(2))    // optional: set expiry
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<String>(verifiedJwt,HttpStatus.OK);
    }

    public ResponseEntity<String> ResendOtp(String token) throws Exception {

        UserDetailsEntity user= findDetailsByJwt(token);



             if (!"ADMIN".equals(user.getRole())) {
            throw new Exception("Invalid Request");
        }
             String Otp =generateOtp();
             user.setOtp(Otp);
             usersRepository.save(user);
             emailServices.sendOtpEmail(user.getEmail(), Otp);

        return new ResponseEntity<String>("Otp SENT  SucessfullY ",HttpStatus.OK);
    }
//




    public UserDetailsEntity createOAuthUserIfNotExists(String email, String name,String mobileno) {
        UserDetailsEntity userDetailsEntity=new UserDetailsEntity();
        if (!usersRepository.existsByEmail(email)) {
            UserDetailsEntity user = new UserDetailsEntity();
            user.setEmail(email);
            user.setMobileno(mobileno);
            user.setName(name);
            user.setPassword(""); // no password for Google users
            user.setRole("USER");
            user.setProvider("GOOGLE");
            userDetailsEntity= usersRepository.save(user);
            return userDetailsEntity;
        }
        return userDetailsEntity;

    }


    public <T> Optional<T> findByEmail(String email) {
        return (Optional<T>) usersRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetailsEntity> user=usersRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User doesn't Exist");
        }

        return new UserPrincipal(user.get());
    }


}
