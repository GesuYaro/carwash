package shagiev.carwash.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserRequestDto {

    private String username;
    private String password;

}
