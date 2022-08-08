package shagiev.carwash.service.availableinterval;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shagiev.carwash.repo.AvailableIntervalRepo;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class IntervalCleanerService {

    private final AvailableIntervalRepo availableIntervalRepo;

    @Scheduled(cron = "${carwash.intervals.delete-cron}")
    public void cleanIntervals() {
        Date date = new Date();
        availableIntervalRepo.deleteAllByFromLessThan(date.getTime());
    }

}
