package shagiev.carwash.dto.carbox.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.model.carbox.CarBox;

@Mapper(componentModel = "spring")
public interface CarBoxInfoDtoMapper extends Converter<CarBox, CarBoxInfoDto> {

    CarBoxInfoDto convertToDto(CarBox carBox);

}
