package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.service.entry.EntryCrudService;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final EntryCrudService entryCrudService;

    @GetMapping("/{id}/entries")
    @PreAuthorize("hasRole('ADMIN')" +
            "|| (hasRole('USER') && @belongsCheckServiceImpl.isEntryBelongsToUser(#id, #principal))")
    public List<EntryInfoDto> getEntries(@PathVariable long id,
                                         @RequestParam(required = false) EntryStatus entryStatus,
                                         @RequestParam(required = false) @Min(0) Integer page,
                                         @RequestParam(required = false) @Min(1) Integer pageSize,
                                         Principal principal) {
        return entryCrudService.getAllByUser(id, entryStatus, page, pageSize);
    }

}
