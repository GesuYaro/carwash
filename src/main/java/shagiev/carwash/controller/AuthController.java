package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.dto.user.AppUserRequestDto;
import shagiev.carwash.service.user.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@PreAuthorize("isAnonymous()")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public AppUserDto register(@RequestBody @Valid AppUserRequestDto requestDto) {
        try {
            return userService.register(requestDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid AppUserRequestDto requestDto) {
    }

}
