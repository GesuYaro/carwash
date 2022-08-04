package shagiev.carwash.service.carwashservice;

import shagiev.carwash.dto.service.CarwashServiceInfoDto;
import shagiev.carwash.dto.service.CarwashServiceRequestDto;

import java.util.List;

public interface CarwashServiceCrudService {

    CarwashServiceInfoDto getConcrete(long id);
    List<CarwashServiceInfoDto> getAll();
    CarwashServiceInfoDto save(CarwashServiceRequestDto requestDto);
    CarwashServiceInfoDto update(long id, CarwashServiceRequestDto requestDto);
    void delete(long id);

}
