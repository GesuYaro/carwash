package shagiev.carwash.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.repo.AppUserRepo;

@Service
@RequiredArgsConstructor
public class CarwashUserDetailsService implements UserDetailsService {

    private final AppUserRepo appUserRepo;
    private final ConversionService conversionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return conversionService.convert(appUser, AppUserSecurityDto.class);
    }
}
