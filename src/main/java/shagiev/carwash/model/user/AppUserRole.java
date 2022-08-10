package shagiev.carwash.model.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static shagiev.carwash.model.user.AppUserPermissions.*;

@Getter
public enum AppUserRole {

    USER(Stream.of(USER_READ, USER_WRITE, USER_DELETE).collect(Collectors.toSet())),
    OPERATOR(Stream.of(OPERATOR_READ, OPERATOR_WRITE).collect(Collectors.toSet())),
    ADMIN(Stream.of(ADMIN_READ, ADMIN_WRITE).collect(Collectors.toSet()));

    private final Set<AppUserPermissions> permissions;

    AppUserRole(Set<AppUserPermissions> permissions) {
        this.permissions = permissions;
    }

}
