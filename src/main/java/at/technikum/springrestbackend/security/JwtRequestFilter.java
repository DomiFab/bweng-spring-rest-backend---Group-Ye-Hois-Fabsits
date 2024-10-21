package at.technikum.springrestbackend.security;

import at.technikum.springrestbackend.services.CustomUserDetailsService;
import at.technikum.springrestbackend.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = null;
        String username = null;
        // JWT-Token zuerst aus dem Cookie extrahieren
        token = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        // JWT-Token aus dem Authorization-Header extrahieren, wenn kein Cookie vorhanden ist
        if (token == null) {
            token = getJwtFromRequest(request);
        }
        if (token != null) {
            try {
                Claims claims = jwtService.extractClaims(token);
                username = claims.getSubject();
            } catch (Exception e) {
                handleInvalidJwt(response); // Ungültiges JWT behandeln
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateUser(request, token, username);
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {// Extrahiert das JWT aus dem Authorization-Header
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // Behandelt die Antwort im Falle eines ungültigen JWT
    private void handleInvalidJwt(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // Authentifiziert den Benutzer und setzt den SecurityContext
    private void authenticateUser(HttpServletRequest request, String token, String username) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
        if (jwtService.validateToken(token, userDetails)) {
            boolean isAdmin = jwtService.extractIsAdmin(token);
            UsernamePasswordAuthenticationToken authToken = buildAuthenticationToken(request, userDetails, isAdmin);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    // Erstellt das Authentifizierungs-Token und setzt die Berechtigungen basierend auf isAdmin-Flag
    private UsernamePasswordAuthenticationToken buildAuthenticationToken(HttpServletRequest request, UserDetails userDetails, boolean isAdmin) {
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        if (isAdmin) {
            authorities.add(new SimpleGrantedAuthority("isAdmin"));
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return token;
    }
}
