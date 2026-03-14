package app.auth.service.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        try {
            // Load keys from resources
            privateKey = loadPrivateKey("Keys/private_pkcs8.key");
            publicKey = loadPublicKey("Keys/public.key");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT keys", e);
        }
    }
    private PrivateKey loadPrivateKey(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream is = resource.getInputStream()) {
            // Read file content as string
            String pem = new String(is.readAllBytes());
            // Remove any header/footer lines and all whitespace
            pem = pem.replaceAll("-----BEGIN (.*) PRIVATE KEY-----", "")
                    .replaceAll("-----END (.*) PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            // Decode base64
            byte[] decoded = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream is = resource.getInputStream()) {
            String pem = new String(is.readAllBytes());
            pem = pem.replaceAll("-----BEGIN (.*) PUBLIC KEY-----", "")
                    .replaceAll("-----END (.*) PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }
    public String generateToken(String username, List<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .claim("roles",roles)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateAdminToken(String username, List<String> roles, boolean otpVerified){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .claim("roles",roles)
                .claim("otpVerified",otpVerified)
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