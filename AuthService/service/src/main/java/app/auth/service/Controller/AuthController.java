package app.auth.service.Controller;


import app.auth.service.DTO.AdminDTO;
import app.auth.service.DTO.LoginDto;
import app.auth.service.DTO.ResponseDto;
import app.auth.service.DTO.Signupdto;
import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Security.JwtUtil;
import app.auth.service.Service.MyUserServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private MyUserServices myUserServices;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(MyUserServices myUserServices, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.myUserServices = myUserServices;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/user/profile")
    public ResponseEntity<Map<String,String>>findUserByJwtMap(@RequestHeader("Authorization") String authHeader) throws Exception {
        String token=authHeader.substring(7);
    return myUserServices.findUserByJwtMap(token);
    }

        @PostMapping("/signup")
    public ResponseEntity<ResponseDto> createUser(@RequestBody Signupdto signupdto){
        return myUserServices.createUser(signupdto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto>loginUser(@RequestBody LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token=jwtUtil.generateToken(loginDto.getEmail(),roles);
//        ResponseCookie cookie = ResponseCookie.from("token", token)
//
//                .httpOnly(true)           // JavaScript cannot read the cookie
//                .secure(true)            // true in production HTTPS
//                .path("/")
//                .maxAge(7 * 24 * 60 * 60) // 7 days
//                .sameSite("Strict")       // mitigate CSRF
//                .build();
        ResponseCookie cookieuser = ResponseCookie.from("usertoken", token)
//
                .httpOnly(false)           // JavaScript cannot read the cookie
                .secure(true)            // true in production HTTPS
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("None")       // mitigate CSRF
                .build();
        return ResponseEntity
                .ok()
//                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .header(HttpHeaders.SET_COOKIE,cookieuser.toString())
                .body(new ResponseDto(token,"Login Sucessfull !"));

    }

    @PostMapping("/user-login")
    public ResponseEntity<ResponseDto>loginUserC(@RequestBody LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token=jwtUtil.generateToken(loginDto.getEmail(),roles);
        ResponseCookie cookie = ResponseCookie.from("token", token)

                .httpOnly(true)           // JavaScript cannot read the cookie
                .secure(true)            // true in production HTTPS
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")       // mitigate CSRF
                .build();
        ResponseCookie cookie1=ResponseCookie.from("login","true")
                .httpOnly(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .header(HttpHeaders.SET_COOKIE,cookie1.toString())
                .body(new ResponseDto("","Login Sucessfull !"));

    }
    @PostMapping("/admin-login")
    public ResponseEntity<ResponseDto>loginAdmin(@RequestBody LoginDto loginDto) throws Exception {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token=jwtUtil.generateToken(loginDto.getEmail(),roles);
        AdminDTO admin=myUserServices.findAdminByJwt(token);

        if (!"ADMIN".equals(admin.getRole())) {
            return new ResponseEntity<ResponseDto>(new ResponseDto(token, "Admin Doesn't exist"), HttpStatus.CONFLICT);
        }
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)               // JS cannot read
                .secure(true)                 // HTTPS only (false in local dev)
                .path("/")                    // sent on all endpoints
                .maxAge(Duration.ofDays(7))   // auto-expires in 7 days
                .sameSite("Strict")           // CSRF protection
                .build();


        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new ResponseDto(token,"Admin Login Sucessfull !"))
                ;

    }

    @PostMapping("/seller-login")
    public ResponseEntity<ResponseDto>loginSeller(@RequestBody LoginDto loginDto) throws Exception {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token=jwtUtil.generateToken(loginDto.getEmail(),roles);
        UserDetailsEntity admin=myUserServices.findDetailsByJwt(token);
        if (!"SELLER".equals(admin.getRole())) {
            return new ResponseEntity<ResponseDto>(new ResponseDto(token, "Student Doesn't exist"), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<ResponseDto>(new ResponseDto(token,"Student Login Sucessfull !"), HttpStatus.ACCEPTED);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin-login/verify-otp")
    public ResponseEntity<String> VERIFYADMINBYOTP(@RequestHeader("Authorization") String authHeader ,@RequestBody String Otp, HttpServletResponse response)throws Exception{
          String token = authHeader.substring(7);
                return myUserServices.VERIFYADMINBYOTP(token,Otp,response);
    }
    @PostMapping("/admin-login/resend-otp")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> RESENDOTP(@RequestHeader("Authorization") String authHeader )throws Exception{
           String token = authHeader.substring(7);
          return myUserServices.ResendOtp(token);
    }
    @GetMapping("/profile")
    public ResponseEntity<Signupdto> profile(@RequestHeader("Authorization") String authHeader )throws Exception{
        String token = authHeader.substring(7);
        return myUserServices.findUserByJwt(token);
    }
}
