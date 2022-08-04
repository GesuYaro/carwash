package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.service.carbox.CarBoxCrudService;

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

    @GetMapping("/{id}")
    public CarBoxInfoDto getConcrete(@PathVariable long id) {
        return carBoxCrudService.getConcrete(id);
    }

    @PostMapping
    public CarBoxInfoDto save(@RequestBody CarBoxRequestDto carBoxRequestDto) {
        return carBoxCrudService.save(carBoxRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        carBoxCrudService.delete(id);
    }

    @PutMapping("/{id}")
    public CarBoxInfoDto update(@PathVariable long id, @RequestBody CarBoxRequestDto carBoxRequestDto) {
        return carBoxCrudService.update(id, carBoxRequestDto);
    }

}
