package shagiev.carwash.service.carwashservice;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.service.CarwashServiceInfoDto;
import shagiev.carwash.dto.service.CarwashServiceRequestDto;
import shagiev.carwash.model.service.CarwashService;
import shagiev.carwash.repo.CarwashServiceRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarwashServiceCrudServiceImpl implements CarwashServiceCrudService {

    private final CarwashServiceRepo carwashServiceRepo;
    private final ConversionService conversionService;

    @Override
    public CarwashServiceInfoDto getConcrete(long id) {
        var carwashService = carwashServiceRepo.findById(id);
        if (carwashService.isPresent()) {
            return conversionService.convert(carwashService.get(), CarwashServiceInfoDto.class);
        } else {
            return null;
        }
    }

    @Override
    public List<CarwashServiceInfoDto> getAll() {
        List<CarwashServiceInfoDto> dtos = new ArrayList<>();
        carwashServiceRepo.findAll().forEach(carwashService ->
                dtos.add(conversionService.convert(carwashService, CarwashServiceInfoDto.class))
        );
        return dtos;
    }

    @Override
    public CarwashServiceInfoDto save(CarwashServiceRequestDto requestDto) {
        CarwashService requestedService = conversionService.convert(requestDto, CarwashService.class);
        if (requestedService == null) {
            return null;
        }
        CarwashService carwashService = carwashServiceRepo.save(requestedService);
        return conversionService.convert(carwashService, CarwashServiceInfoDto.class);
    }

    @Override
    public CarwashServiceInfoDto update(long id, CarwashServiceRequestDto requestDto) {
        CarwashService requestedService = conversionService.convert(requestDto, CarwashService.class);
        if (requestedService != null) {
            requestedService.setId(id);
            if (carwashServiceRepo.existsById(id)) {
                CarwashService carwashService = carwashServiceRepo.save(requestedService);
                return conversionService.convert(carwashService, CarwashServiceInfoDto.class);
            }
        }
        return null;
    }

    @Override
    public void delete(long id) {
        carwashServiceRepo.deleteById(id);
    }

}
