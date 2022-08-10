package shagiev.carwash.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import shagiev.carwash.model.user.AppUserRole;

@Data
@AllArgsConstructor
public class AppUserDto {

    private long id;
    private String username;
    private AppUserRole role;

}
