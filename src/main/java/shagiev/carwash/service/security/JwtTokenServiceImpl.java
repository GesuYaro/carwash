package shagiev.carwash.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${carwash.security.bearer}")
    private String bearer;

    @Value("${carwash.security.secure-key}")
    private String secureKey;

    @Value("${carwash.security.auth-claim}")
    private String authClaimName;

    @Value("${carwash.security.auth-header-name}")
    private String accessTokenHeader;

    @Value("${carwash.security.refresh-header-name}")
    private String refreshTokenHeader;

    @Value("${carwash.security.access-token.days}")
    private long accessTokenDays;

    @Value("${carwash.security.refresh-token.days}")
    private long refreshTokenDays;

    @Override
    public String generateAccessToken(Authentication authResult) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(accessTokenDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim(authClaimName, authResult.getAuthorities())
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }

    @Override
    public String generateRefreshToken(Authentication authResult) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(refreshTokenDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim(authClaimName, authResult.getAuthorities())
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        var auth = (List<Map<String, String>>) claims.get(authClaimName);
        return auth.stream()
                .map(map -> new SimpleGrantedAuthority(map.get("authority")))
                .collect(Collectors.toSet());
    }

    @Override
    public Claims extractClaims(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();
        jwtParserBuilder.setSigningKey(Keys.hmacShaKeyFor(secureKey.getBytes()));
        Jws<Claims> jws = jwtParserBuilder.build().parseClaimsJws(token);
        Claims claims = jws.getBody();
        if (claims
                .getExpiration()
                .before(
                        Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                )
        ) {
            throw new ExpiredJwtException(jws.getHeader(), jws.getBody(), "JWT token is expired");
        }
        return claims;
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.replace(bearer, "");
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(refreshTokenHeader);
        if (authHeader == null || !authHeader.startsWith(bearer)) {
            throw new IllegalArgumentException("Refresh token is missing");
        }

        Claims claims = extractClaims(authHeader);

        response.setHeader(refreshTokenHeader, generateRefreshToken(claims));
        response.setHeader(accessTokenHeader, generateAccessToken(claims));
    }

    private String generateAccessToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(accessTokenDays)))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }

    private String generateRefreshToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(refreshTokenDays)))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }
}
