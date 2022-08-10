package shagiev.carwash.model.user;

import lombok.Getter;

@Getter
public enum AppUserPermissions {

    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    OPERATOR_READ("operator:read"),
    OPERATOR_WRITE("operator:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String permission;

    AppUserPermissions(String permission) {
        this.permission = permission;
    }
}
