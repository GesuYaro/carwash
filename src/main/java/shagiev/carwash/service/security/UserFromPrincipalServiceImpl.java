package shagiev.carwash.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.repo.AppUserRepo;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserFromPrincipalServiceImpl implements UserFromPrincipalService {

    private final AppUserRepo appUserRepo;

    @Override
    public AppUser getUser(Principal principal) {
        String name = principal.getName();
        return appUserRepo.findByUsername(name);
    }
}
