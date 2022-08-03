package shagiev.carwash.dto.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.service.CarwashServiceInfoDto;
import shagiev.carwash.model.service.CarwashService;

@Mapper(componentModel = "spring")
public interface CarwashServiceInfoDtoMapper extends Converter<CarwashService, CarwashServiceInfoDto> {

    CarwashServiceInfoDto convertToDto(CarwashService carwashService);

}
