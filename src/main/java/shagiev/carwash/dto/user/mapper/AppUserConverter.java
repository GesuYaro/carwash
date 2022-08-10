package shagiev.carwash.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.user.AppUserRequestDto;
import shagiev.carwash.model.user.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserConverter extends Converter<AppUserRequestDto, AppUser> {

    @Mapping(target = "id", constant = "0L")
    @Mapping(target = "role", constant = "USER")
    AppUser convert(AppUserRequestDto requestDto);

}
