package shagiev.carwash.dto.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.model.user.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserDtoConverter extends Converter<AppUser, AppUserDto> {

    AppUserDto convert(AppUser appUser);

}
