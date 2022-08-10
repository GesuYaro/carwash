package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shagiev.carwash.dto.carbox.CarBoxInfoDto;
import shagiev.carwash.dto.carbox.CarBoxRequestDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.service.carbox.CarBoxCrudService;
import shagiev.carwash.service.entry.EntryCrudService;

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
        return carBoxCrudService.getConcrete(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CarBoxInfoDto save(@Validated @RequestBody CarBoxRequestDto carBoxRequestDto) {
        return carBoxCrudService.save(carBoxRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        carBoxCrudService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarBoxInfoDto update(@PathVariable long id, @Validated @RequestBody CarBoxRequestDto carBoxRequestDto) {
        return carBoxCrudService.update(id, carBoxRequestDto);
    }

    @GetMapping("/{id}/entries")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isCarBoxBelongsToOperator(#id, #appUserSecurityDto.id))")
    public List<EntryInfoDto> getEntriesByCarBox(@PathVariable long id,
                                                 @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        return entryCrudService.getAllByCarBox(id);
    }

}
