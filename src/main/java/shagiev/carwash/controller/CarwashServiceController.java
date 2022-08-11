package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.service.CarwashServiceInfoDto;
import shagiev.carwash.dto.service.CarwashServiceRequestDto;
import shagiev.carwash.service.carwashservice.CarwashServiceCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class CarwashServiceController {

    private final CarwashServiceCrudService crudService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public List<CarwashServiceInfoDto> getAll() {
        try {
            return crudService.getAll();
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public CarwashServiceInfoDto getConcrete(@PathVariable long id) {
        try {
            return crudService.getConcrete(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CarwashServiceInfoDto save(@RequestBody @Valid CarwashServiceRequestDto requestDto) {
        try {
            return crudService.save(requestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarwashServiceInfoDto update(@PathVariable long id, @RequestBody @Valid CarwashServiceRequestDto requestDto) {
        try {
            return crudService.update(id, requestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        try {
            crudService.delete(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
