package app.Ecommerce.ProductServiceApp.Security;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

   public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

        String AuthHeader=request.getHeader("Authorization");
        String username=null;
String token=null;

if(AuthHeader!=null&&AuthHeader.startsWith("Bearer ")){
    token=AuthHeader.substring(7);
    username=jwtUtil.extractUsername(token);

}

if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){

    List<String> roles=jwtUtil.extractRoles(token);
    List<SimpleGrantedAuthority>authorities=roles.stream().map(role->new SimpleGrantedAuthority(role)).toList();

    UserDetails userDetails=new User(username,"",authorities);
    if(jwtUtil.isTokenValid(token,userDetails.getUsername())){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,authorities);


        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("AUTH SET: " +
                SecurityContextHolder.getContext().getAuthentication());

    }
}

        filterChain.doFilter(request,response);
    }
}
