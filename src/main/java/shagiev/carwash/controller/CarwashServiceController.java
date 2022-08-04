package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shagiev.carwash.dto.service.CarwashServiceInfoDto;
import shagiev.carwash.dto.service.CarwashServiceRequestDto;
import shagiev.carwash.service.carwashservice.CarwashServiceCrudService;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class CarwashServiceController {

    private final CarwashServiceCrudService crudService;

    @GetMapping
    public List<CarwashServiceInfoDto> getAll() {
        return crudService.getAll();
    }

    @GetMapping("/{id}")
    public CarwashServiceInfoDto getConcrete(@PathVariable long id) {
        return crudService.getConcrete(id);
    }

    @PostMapping
    public CarwashServiceInfoDto save(@RequestBody CarwashServiceRequestDto requestDto) {
        return crudService.save(requestDto);
    }

    @PutMapping("/{id}")
    public CarwashServiceInfoDto update(@PathVariable long id, @RequestBody CarwashServiceRequestDto requestDto) {
        return crudService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        crudService.delete(id);
    }

}
