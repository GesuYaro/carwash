package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.service.CarBoxCrudService;

import java.util.List;

@RestController
@RequestMapping("/car-boxes")
@RequiredArgsConstructor
public class CarBoxController {

    private final CarBoxCrudService carBoxCrudService;

    @GetMapping
    public List<CarBoxInfoDto> getAll() {
        return carBoxCrudService.getAll();
    }

    @PostMapping
    public CarBoxInfoDto save(@RequestBody CarBoxRequestDto carBoxRequestDto) {
        return carBoxCrudService.save(carBoxRequestDto);
    }

}
