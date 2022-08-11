package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.availableinterval.IntervalRequestDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryUserRequestDto;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.service.availableinterval.EnrollService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enroll")
@RequiredArgsConstructor
public class EnrollController {

    private final EnrollService enrollService;

    @GetMapping("/available-intervals")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public List<AvailableIntervalDto> getAvailableIntervals(@Valid IntervalRequestDto intervalRequestDto) {
        return enrollService.getAvailableIntervals(intervalRequestDto.getDate(), intervalRequestDto.getServiceId());
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #appUserSecurityDto.id))" +
            "|| (hasAnyRole('USER', 'OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #appUserSecurityDto.id))")
    public EntryInfoDto confirmEnroll(@PathVariable long id,
                                      @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return enrollService.confirm(id, appUserSecurityDto.getId());
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public EntryInfoDto enroll(@RequestBody @Valid EntryUserRequestDto entryUserRequestDto,
                               @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return enrollService.makeEntry(
                    entryUserRequestDto.getDate(),
                    entryUserRequestDto.getServiceId(),
                    appUserSecurityDto.getId()
            );
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/finish/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #appUserSecurityDto.id))")
    public EntryInfoDto finish(@PathVariable long id,
                               @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return enrollService.freeEntry(id, EntryStatus.FINISHED, appUserSecurityDto.getId());
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #appUserSecurityDto.id))" +
            "|| (hasAnyRole('USER', 'OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #appUserSecurityDto.id))")
    public EntryInfoDto cancel(@PathVariable long id,
                               @AuthenticationPrincipal AppUserSecurityDto appUserSecurityDto) {
        try {
            return enrollService.freeEntry(id, EntryStatus.CANCELED, appUserSecurityDto.getId());
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


}
