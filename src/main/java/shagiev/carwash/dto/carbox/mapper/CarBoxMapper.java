package shagiev.carwash.dto.carbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.model.carbox.CarBox;

@Mapper(componentModel = "spring")
public interface CarBoxMapper extends Converter<CarBoxRequestDto, CarBox> {

    @Mapping(target = "id", constant = "0L")
    CarBox convertToEntity(CarBoxRequestDto carBoxDto);

}
