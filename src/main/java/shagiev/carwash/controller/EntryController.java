package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.user.AppUserSecurityDto;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;
import shagiev.carwash.service.util.DateParserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryCrudService entryCrudService;
    private final DateParserService dateParserService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')" +
            "|| (hasRole('OPERATOR') && @belongsCheckServiceImpl.isCarBoxBelongsToOperator(#carboxId, #principal))")
    public List<EntryInfoDto> getAll(Principal principal,
                                     @RequestParam(required = false) @Min(1) Long carboxId,
                                     @RequestParam(required = false) @Pattern(regexp = "\\d{4}-[0,1]\\d-[0-3]\\d") String from,
                                     @RequestParam(required = false) @Pattern(regexp = "\\d{4}-[0,1]\\d-[0-3]\\d") String until,
                                     @RequestParam(required = false) @Min(0) Integer page,
                                     @RequestParam(required = false) @Min(1) Integer pageSize) {
        try {
            return entryCrudService.getAll(
                    carboxId,
                    dateParserService.getDate(from),
                    dateParserService.getDate(until),
                    page,
                    pageSize
            );
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
