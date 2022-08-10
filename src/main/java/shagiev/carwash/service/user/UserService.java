package shagiev.carwash.service.user;

import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.dto.user.AppUserRequestDto;

public interface UserService {

    AppUserDto register(AppUserRequestDto requestDto);


}
