package cart.service.CartServiceApp.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private PublicKey publicKey;


    @Value("${public_key}")
    private String publicKeyStr;

    @PostConstruct
    public void init() {
        try {
            publicKey = loadPublicKeyFromBase64(publicKeyStr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT keys", e);
        }
    }


    private PublicKey loadPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }


    public Boolean isTokenValid(String token, String username){
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }


    public Long extractUserId(String token){
        return extractAllClaims(token, claims -> claims.get("userId", Long.class));
    }

    public String extractUsername(String token){
        return extractAllClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public List<String> extractRoles(String token){
        return extractAllClaims(token, claims -> claims.get("roles", List.class));
    }

    public <T> T extractAllClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}