package app.Ecommerce.ProductServiceApp.Security;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ResilienceRateLimiter rateLimiter = new ResilienceRateLimiter();

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;

        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                username = jwtUtil.extractUsername(token);
            }
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        String rateKey = (username != null) ? username : request.getRemoteAddr();

        try {
            rateLimiter.getRateLimiter(rateKey).acquirePermission();
        } catch (RequestNotPermitted ex) {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                Long userId = jwtUtil.extractUserId(token);
                List<String> roles = jwtUtil.extractRoles(token);

                if (!jwtUtil.isTokenExpired(token)) {

                    UserPrincipal principal =
                            new UserPrincipal(userId, username, roles);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    principal.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception ignored) {}
        }

        filterChain.doFilter(request, response);
    }
}