package shagiev.carwash.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.dto.user.AppUserRequestDto;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.repo.AppUserRepo;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepo appUserRepo;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto register(AppUserRequestDto requestDto) {
        AppUser user = conversionService.convert(requestDto, AppUser.class);
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("user already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = appUserRepo.save(user);
        return conversionService.convert(savedUser, AppUserDto.class);
    }

}
