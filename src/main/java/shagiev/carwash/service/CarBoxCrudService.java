package shagiev.carwash.service;

import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;

import java.util.List;

public interface CarBoxCrudService {

    List<CarBoxInfoDto> getAll();
    CarBoxInfoDto save(CarBoxRequestDto carBoxRequestDto);

}
