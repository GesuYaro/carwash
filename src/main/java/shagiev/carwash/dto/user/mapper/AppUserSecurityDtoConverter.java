package shagiev.carwash.dto.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.model.user.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserSecurityDtoConverter extends Converter<AppUser, AppUserSecurityDto> {

    AppUserSecurityDto convert(AppUser appUser);

}
