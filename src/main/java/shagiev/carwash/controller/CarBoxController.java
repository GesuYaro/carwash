package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.service.carbox.CarBoxCrudService;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car-boxes")
@RequiredArgsConstructor
public class CarBoxController {

    private final CarBoxCrudService carBoxCrudService;
    private final EntryCrudService entryCrudService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CarBoxInfoDto> getAll() {
        return carBoxCrudService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isCarBoxBelongsToOperator(#id, #appUserSecurityDto.id))")
    public CarBoxInfoDto getConcrete(@PathVariable long id,
                                     @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return carBoxCrudService.getConcrete(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CarBoxInfoDto save(@Valid @RequestBody CarBoxRequestDto carBoxRequestDto) {
        try {
            return carBoxCrudService.save(carBoxRequestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        try {
            carBoxCrudService.delete(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarBoxInfoDto update(@PathVariable long id, @Valid @RequestBody CarBoxRequestDto carBoxRequestDto) {
        try {
            return carBoxCrudService.update(id, carBoxRequestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}/entries")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isCarBoxBelongsToOperator(#id, #appUserSecurityDto.id))")
    public List<EntryInfoDto> getEntriesByCarBox(@PathVariable long id,
                                                 @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return entryCrudService.getAllByCarBox(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
