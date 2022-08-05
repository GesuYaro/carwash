package shagiev.carwash.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.availableinterval.IntervalRequestDto;

import java.util.List;

@RestController
@RequestMapping("/enroll")
public class EntryController {

    @GetMapping("/available-intervals")
    public List<AvailableIntervalDto> getAvailableIntervals(IntervalRequestDto intervalRequestDto) {
        System.out.println(intervalRequestDto);
        return null;
    }



}
