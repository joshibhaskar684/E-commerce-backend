package app.auth.service.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
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

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Value("${private_key}")
    private String privateKeyStr;

    @Value("${public_key}")
    private String publicKeyStr;

    @PostConstruct
    public void init() {
        try {
            privateKey = loadPrivateKeyFromBase64(privateKeyStr);
            publicKey = loadPublicKeyFromBase64(publicKeyStr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT keys", e);
        }
    }

    private PrivateKey loadPrivateKeyFromBase64(String base64Key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    // JWT methods remain unchanged
    public String generateToken(String username, List<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .claim("roles", roles)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateAdminToken(String username, List<String> roles, boolean otpVerified){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .claim("roles", roles)
                .claim("otpVerified", otpVerified)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Boolean isTokenValid(String token, String username){
        return extractUsername(token).equals(username) && !isTokenExpired(token);
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