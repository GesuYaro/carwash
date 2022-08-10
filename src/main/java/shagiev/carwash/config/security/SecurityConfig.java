package shagiev.carwash.config.security;

import lombok.RequiredArgsConstructor;
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
import shagiev.carwash.service.security.JwtTokenService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${carwash.security.bearer}")
    private String bearer;

    @Value("${carwash.security.auth-header-name}")
    private String headerName;

    @Value("${carwash.security.access-token.name}")
    private String accessTokenName;

    @Value("${carwash.security.refresh-token.name}")
    private String refreshTokenName;

    private final JwtTokenService jwtTokenService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(6);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter("/auth/login", this.authenticationManager(), jwtTokenService);
        JwtValidator jwtValidator = new JwtValidator(new String[]{"/auth/register", "/refresh-token"}, jwtTokenService);
        init(jwtAuthFilter, jwtValidator);

        http
                .csrf().disable()
                .cors().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/*", "/refresh-token")
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidator, JwtAuthFilter.class);
    }

    private void init(JwtAuthFilter jwtAuthFilter, JwtValidator jwtValidator) {
        jwtAuthFilter.setAccessTokenName(accessTokenName);
        jwtAuthFilter.setRefreshTokenName(refreshTokenName);

        jwtValidator.setBearer(bearer);
        jwtValidator.setHeaderName(headerName);
    }
}
