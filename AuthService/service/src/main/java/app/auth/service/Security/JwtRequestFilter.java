package app.auth.service.Security;

import app.auth.service.Service.MyUserServices;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private MyUserServices myUserServices;
    private final ResilienceRateLimiter rateLimiter = new ResilienceRateLimiter();
    private static final int SC_TOO_MANY_REQUESTS = 429;
    public JwtRequestFilter(JwtUtil jwtUtil, MyUserServices myUserServices) {
        this.jwtUtil = jwtUtil;
        this.myUserServices = myUserServices;
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

        String ratekeys = (username != null) ? username : request.getRemoteAddr();

        RateLimiter rl = rateLimiter.getRateLimiter(ratekeys);

        try {
            rl.acquirePermission(); // throws if limit exceeded
        } catch (RequestNotPermitted ex) {

            response.setStatus(SC_TOO_MANY_REQUESTS);
            response.getWriter().write("Too many requests");
            return;
        }
if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
    UserDetails userDetails=myUserServices.loadUserByUsername(username);
    List<String> roles=jwtUtil.extractRoles(token);
    List<SimpleGrantedAuthority>authorities=roles.stream().map(role->new SimpleGrantedAuthority(role)).toList();
    if(jwtUtil.isTokenValid(token,userDetails.getUsername())){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    }
}

        filterChain.doFilter(request,response);
    }
}
