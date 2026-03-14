package app.auth.service.Security;

import app.auth.service.Entity.UserDetailsEntity;
import app.auth.service.Service.MyUserServices;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MyUserServices myUserServices;

    @Value("${redirect_URL}")
    private String redirectUrl;

    public OAuth2SuccessHandler( MyUserServices myUserServices, JwtUtil jwtUtil) {
        this.myUserServices = myUserServices;
        this.jwtUtil = jwtUtil;
    }

    @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, java.io.IOException {

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            // Load or create user in DB
            UserDetailsEntity userEntity = (UserDetailsEntity) myUserServices.findByEmail(email)
                    .orElseGet(() -> myUserServices.createOAuthUserIfNotExists(email, oAuth2User.getAttribute("name"), ""));

            UserDetails userDetails = new UserPrincipal(userEntity);

            // Extract roles
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // Generate JWT
            String token = jwtUtil.generateToken(email, roles);

            // Send JWT in cookie
            ResponseCookie cookie = ResponseCookie.from("usertoken", token)
//                    .domain(".vhbuyio.site")
                    .httpOnly(false)      // true if you want JS to read it
                    .secure(true)        // true in production HTTPS
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

//            String redirectUrl = request.getParameter("redirect_uri");
//
//            String fallbackUrl = "https://academy.vhbuyio.site/select-app";
//
//




        response.sendRedirect(redirectUrl);

        }


}
