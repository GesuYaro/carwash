package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.service.entry.EntryCrudService;

import java.util.List;

@RestController
@RequestMapping("/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryCrudService entryCrudService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EntryInfoDto> getAll() {
        return entryCrudService.getAll();
    }

}
