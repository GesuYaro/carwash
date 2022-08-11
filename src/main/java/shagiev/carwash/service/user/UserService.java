package shagiev.carwash.service.user;

import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.dto.user.AppUserRequestDto;
import shagiev.carwash.dto.user.operator.OperatorInfoDto;
import shagiev.carwash.dto.user.operator.OperatorRequestDto;

public interface UserService {

    AppUserDto register(AppUserRequestDto requestDto);
    OperatorInfoDto makeUserOperator(OperatorRequestDto requestDto);
    OperatorInfoDto updateOperator(OperatorRequestDto requestDto);


}
