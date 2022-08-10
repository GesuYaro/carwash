package shagiev.carwash.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import shagiev.carwash.dto.user.AppUserRequestDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Setter
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
    }

    @Value("${carwash.security.secure-key}")
    private String secureKey;

    @Value("${carwash.security.access-token.name}")
    private String accessTokenName;

    @Value("${carwash.security.refresh-token.name}")
    private String refreshTokenName;

    @Value("${carwash.security.auth-claim}")
    private String authClaimName;

    @Value("${carwash.security.access-token.days}")
    private long accessTokenDays;

    @Value("${carwash.security.refresh-token.days}")
    private long refreshTokenDays;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AppUserRequestDto requestDto = new ObjectMapper()
                    .readValue(request.getInputStream(), AppUserRequestDto.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        generateBody(response, authResult);
    }

    private void generateBody(HttpServletResponse response, Authentication authResult) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(accessTokenName, generateAccessToken(authResult));
        tokens.put(refreshTokenName, generateRefreshToken(authResult));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    private String generateAccessToken(Authentication authResult) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(accessTokenDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim(authClaimName, authResult.getAuthorities())
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }

    private String generateRefreshToken(Authentication authResult) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(refreshTokenDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim(authClaimName, authResult.getAuthorities())
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secureKey.getBytes()))
                .compact();
    }

}
