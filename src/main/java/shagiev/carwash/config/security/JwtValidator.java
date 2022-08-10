package shagiev.carwash.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shagiev.carwash.service.security.JwtTokenService;
import shagiev.carwash.service.security.JwtTokenServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
public class JwtValidator extends OncePerRequestFilter {

    @Value("${carwash.security.bearer}")
    private String bearer;

    @Value("${carwash.security.auth-header-name}")
    private String headerName;

    private final String[] shouldNotFilterUrls;

    private final JwtTokenService jwtTokenService;

    public JwtValidator(String[] shouldNotFilterUrls, JwtTokenService jwtTokenService) {
        this.shouldNotFilterUrls = shouldNotFilterUrls;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(headerName);
        if (Strings.isEmpty(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!authorizationHeader.startsWith(bearer)) {
            filterChain.doFilter(request, response);
            return;
        }
        doAuthenticate(request);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (String url: shouldNotFilterUrls) {
            if (request.getRequestURI().equals(url)) {
                return true;
            }
        }
        return false;
    }

    private void doAuthenticate(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(headerName);
        try {
            Claims claims = extractClaims(authorizationHeader);
            String username = claims.getSubject();
            Set<SimpleGrantedAuthority> authorities = getAuthorities(claims);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new MalformedJwtException(String.format("Can not authorize token: %s", authorizationHeader));
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return jwtTokenService.getAuthorities(claims);
    }

    private Claims extractClaims(String authorizationHeader) {
        return jwtTokenService.extractClaims(authorizationHeader);
    }

}
