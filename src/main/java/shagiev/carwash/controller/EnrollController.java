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
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.service.availableinterval.EnrollService;
import shagiev.carwash.service.exceptions.NoSuchIdException;
import shagiev.carwash.service.security.UserFromPrincipalService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/enroll")
@RequiredArgsConstructor
public class EnrollController {

    private final EnrollService enrollService;
    private final UserFromPrincipalService userFromPrincipalService;

    @GetMapping("/available-intervals")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public List<AvailableIntervalDto> getAvailableIntervals(@Valid IntervalRequestDto intervalRequestDto) {
        return enrollService.getAvailableIntervals(intervalRequestDto.getDate(), intervalRequestDto.getServiceId());
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #principal))" +
            "|| (hasAnyRole('USER', 'OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #principal))")
    public EntryInfoDto confirmEnroll(@PathVariable long id,
                                      Principal principal) {
        try {
            return enrollService.confirm(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'USER')")
    public EntryInfoDto enroll(@RequestBody @Valid EntryUserRequestDto entryUserRequestDto,
                               Principal principal) {
        AppUser user = userFromPrincipalService.getUser(principal);
        try {
            return enrollService.makeEntry(
                    entryUserRequestDto.getDate(),
                    entryUserRequestDto.getServiceId(),
                    user.getId()
            );
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #principal))" +
            "|| (hasAnyRole('USER', 'OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #principal))")
    public EntryInfoDto update(@PathVariable long id,
                               @RequestBody @Valid EntryUserRequestDto entryUserRequestDto,
                               Principal principal) {
        try {
            return enrollService.update(id, entryUserRequestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/finish/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #principal))")
    public EntryInfoDto finish(@PathVariable long id,
                               Principal principal) {
        try {
            return enrollService.freeEntry(id, EntryStatus.FINISHED);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') " +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToOperator(#id, #principal))" +
            "|| (hasAnyRole('USER', 'OPERATOR') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #principal))")
    public EntryInfoDto cancel(@PathVariable long id,
                               Principal principal) {
        try {
            return enrollService.freeEntry(id, EntryStatus.CANCELED);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


}
