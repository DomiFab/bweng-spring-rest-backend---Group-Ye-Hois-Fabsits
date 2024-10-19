package at.technikum.springrestbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private String SECRET_KEY = "deinGeheimesSchluessel"; // Setze hier deinen geheimen Schlüssel


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 100)) // 100 Stunden
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Die neue Methode zur Validierung des Tokens
    public boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = extractClaims(token).getSubject();
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean extractIsAdmin(String token) {
        Claims claims = extractClaims(token);
        // Annahme: Das JWT enthält ein Claim namens "isAdmin", das einen Boolean-Wert speichert
        return claims.get("isAdmin", Boolean.class);
    }

}
