package shagiev.carwash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.repo.CarBoxRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBoxCrudServiceImpl implements CarBoxCrudService {

    private final CarBoxRepo carBoxRepo;
    private final ConversionService conversionService;

    @Override
    public List<CarBoxInfoDto> getAll() {
        List<CarBoxInfoDto> dtos = new ArrayList<>();
        carBoxRepo.findAll().forEach(carBox ->
                dtos.add(conversionService.convert(carBox, CarBoxInfoDto.class))
        );
        return dtos;
    }

    @Override
    public CarBoxInfoDto save(CarBoxRequestDto carBoxRequestDto) {
        CarBox requestedCarBox = conversionService.convert(carBoxRequestDto, CarBox.class);
        if (requestedCarBox == null) {
            return null;
        }
        CarBox carBox = carBoxRepo.save(requestedCarBox);
        return conversionService.convert(carBox, CarBoxInfoDto.class);
    }
}
