package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shagiev.carwash.service.security.JwtTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/refresh-token")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtTokenService jwtTokenService;

    @PostMapping
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        jwtTokenService.refresh(request, response);
        return ResponseEntity.ok().build();
    }

}
