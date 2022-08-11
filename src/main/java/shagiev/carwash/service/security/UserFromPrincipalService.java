package shagiev.carwash.service.security;

import shagiev.carwash.model.user.AppUser;

import java.security.Principal;

public interface UserFromPrincipalService {

    AppUser getUser(Principal principal);

}
