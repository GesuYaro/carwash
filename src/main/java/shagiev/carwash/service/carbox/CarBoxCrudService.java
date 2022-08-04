package shagiev.carwash.service.carbox;

import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;

import java.util.List;

public interface CarBoxCrudService {

    List<CarBoxInfoDto> getAll();
    CarBoxInfoDto getConcrete(long id);
    CarBoxInfoDto save(CarBoxRequestDto carBoxRequestDto);
    void delete(long id);
    CarBoxInfoDto update(long id, CarBoxRequestDto carBoxRequestDto);

}
