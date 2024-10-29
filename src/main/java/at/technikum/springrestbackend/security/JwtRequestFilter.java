package at.technikum.springrestbackend.security;

import at.technikum.springrestbackend.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);
        String username = null;

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                handleInvalidJwt(response); // Handle invalid JWT
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateUser(request, jwt, username);
        }

        chain.doFilter(request, response);
    }

    // Extracts the JWT from the Authorization header
    private String getJwtFromRequest(HttpServletRequest request) {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // Handles the response in case of an invalid JWT
    private void handleInvalidJwt(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // Authenticates the user and sets the security context
    private void authenticateUser(HttpServletRequest request, String jwt, String username) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(jwt, userDetails)) {
            boolean isAdmin = jwtUtil.extractIsAdmin(jwt);
            UsernamePasswordAuthenticationToken authToken = buildAuthenticationToken(request, userDetails, isAdmin);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    // Builds the authentication token and sets authorities based on isAdmin flag
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
