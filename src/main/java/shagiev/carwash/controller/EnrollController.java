package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.availableinterval.IntervalRequestDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryUserRequestDto;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.service.availableinterval.EnrollService;

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

    @PostMapping
    public EntryInfoDto enroll(@RequestBody EntryUserRequestDto entryUserRequestDto) {
        return enrollService.makeEntry(entryUserRequestDto.getDate(), entryUserRequestDto.getServiceId());
    }

    @DeleteMapping("/{id}")
    public EntryInfoDto cancel(@PathVariable long id) {
        return enrollService.freeEntry(id, EntryStatus.CANCELED);
    }


}
