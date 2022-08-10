package shagiev.carwash.service.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public interface JwtTokenService {

    String generateAccessToken(Authentication authResult);
    String generateRefreshToken(Authentication authResult);
    Set<SimpleGrantedAuthority> getAuthorities(Claims claims);
    Claims extractClaims(String authorizationHeader);
    void refresh(HttpServletRequest request, HttpServletResponse response);
}
