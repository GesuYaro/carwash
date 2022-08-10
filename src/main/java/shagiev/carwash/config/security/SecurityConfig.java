package shagiev.carwash.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${carwash.security.bearer}")
    private String bearer;

    @Value("${carwash.security.auth-header-name}")
    private String headerName;

    @Value("${carwash.security.secure-key}")
    private String secureKey;

    @Value("${carwash.security.auth-claim}")
    private String authClaimName;

    @Value("${carwash.security.access-token.name}")
    private String accessTokenName;

    @Value("${carwash.security.refresh-token.name}")
    private String refreshTokenName;

    @Value("${carwash.security.access-token.days}")
    private long accessTokenDays;

    @Value("${carwash.security.refresh-token.days}")
    private long refreshTokenDays;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(6);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter("/auth/login", this.authenticationManager());
        JwtValidator jwtValidator = new JwtValidator("/auth/register");
        init(jwtAuthFilter, jwtValidator);

        http
                .csrf().disable()
                .cors().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/*")
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidator, JwtAuthFilter.class);
    }

    private void init(JwtAuthFilter jwtAuthFilter, JwtValidator jwtValidator) {
        jwtAuthFilter.setSecureKey(secureKey);
        jwtAuthFilter.setAccessTokenName(accessTokenName);
        jwtAuthFilter.setRefreshTokenName(refreshTokenName);
        jwtAuthFilter.setAuthClaimName(authClaimName);
        jwtAuthFilter.setAccessTokenDays(accessTokenDays);
        jwtAuthFilter.setRefreshTokenDays(refreshTokenDays);

        jwtValidator.setBearer(bearer);
        jwtValidator.setHeaderName(headerName);
        jwtValidator.setSecureKey(secureKey);
        jwtValidator.setAuthClaimName(authClaimName);
    }
}
