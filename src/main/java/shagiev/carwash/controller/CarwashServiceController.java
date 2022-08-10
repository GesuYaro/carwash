package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public List<CarwashServiceInfoDto> getAll() {
        return crudService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public CarwashServiceInfoDto getConcrete(@PathVariable long id) {
        return crudService.getConcrete(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CarwashServiceInfoDto save(@RequestBody CarwashServiceRequestDto requestDto) {
        return crudService.save(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarwashServiceInfoDto update(@PathVariable long id, @RequestBody CarwashServiceRequestDto requestDto) {
        return crudService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        crudService.delete(id);
    }

}
