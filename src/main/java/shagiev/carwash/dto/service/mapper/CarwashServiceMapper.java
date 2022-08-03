package shagiev.carwash.dto.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.service.CarwashServiceRequestDto;
import shagiev.carwash.model.service.CarwashService;

@Mapper(componentModel = "spring")
public interface CarwashServiceMapper extends Converter<CarwashServiceRequestDto, CarwashService> {

    CarwashService convertToEntity(CarwashServiceRequestDto carwashService);

}
