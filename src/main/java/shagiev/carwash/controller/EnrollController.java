package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.availableinterval.IntervalRequestDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryUserRequestDto;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.service.availableinterval.EnrollService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import java.util.List;

@RestController
@RequestMapping("/enroll")
@RequiredArgsConstructor
public class EnrollController {

    private final EnrollService enrollService;

    @GetMapping("/available-intervals")
    public List<AvailableIntervalDto> getAvailableIntervals(IntervalRequestDto intervalRequestDto) {
        return enrollService.getAvailableIntervals(intervalRequestDto.getDate(), intervalRequestDto.getServiceId());
    }

    @GetMapping("/confirm/{id}")
    public EntryInfoDto confirmEnroll(@PathVariable long id) {
        try {
            return enrollService.confirm(id);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public EntryInfoDto enroll(@RequestBody EntryUserRequestDto entryUserRequestDto) {
        try {
            return enrollService.makeEntry(entryUserRequestDto.getDate(), entryUserRequestDto.getServiceId());
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public EntryInfoDto cancel(@PathVariable long id) {
        try {
            return enrollService.freeEntry(id, EntryStatus.CANCELED);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
