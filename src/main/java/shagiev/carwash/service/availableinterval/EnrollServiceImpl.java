package shagiev.carwash.service.availableinterval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryRequestDto;
import shagiev.carwash.model.availableinterval.AvailableInterval;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.model.service.CarwashService;
import shagiev.carwash.repo.CarwashServiceRepo;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.carwashservice.CarwashServiceCrudService;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollServiceImpl implements EnrollService {

    private final AvailableIntervalsService intervalsService;
    private final CarwashServiceCrudService carwashServiceCrudService;
    private final CarwashServiceRepo carwashServiceRepo;
    private final IntervalLinkerService intervalLinkerService;
    private final EntryCrudService entryCrudService;
    private final EntryRepo entryRepo;

    @Override
    public List<AvailableIntervalDto> getAvailableIntervals(Date date, long serviceId) {
        Duration baseDuration = carwashServiceCrudService.getConcrete(serviceId).getDuration();
        return intervalLinkerService.compose(intervalsService.getAvailableIntervalsForDay(baseDuration, date), baseDuration);
    }

    @Override
    public EntryInfoDto makeEntry(Date date, long serviceId) {
        Duration baseDuration = carwashServiceCrudService.getConcrete(serviceId).getDuration();
        List<AvailableInterval> intervals = intervalsService.getIntervalsForConcreteTime(baseDuration, date);
        if (intervals.isEmpty()) {
            return null;
        }
        AvailableInterval biggestInterval = intervals.stream()
                .max(Comparator.comparing((i) -> i.getUntil() - i.getFrom()))
                .orElseThrow(() -> new IllegalArgumentException("No interval for entry"));
        Date until = new Date(date.getTime()
                + (long) (baseDuration.toMillis() * biggestInterval.getCarBox().getTimeCoefficient()));
        intervalsService.busyInterval(biggestInterval.getId(), date, until);
        CarwashService service = carwashServiceRepo.getReferenceById(serviceId);
        EntryRequestDto entryRequestDto = new EntryRequestDto(serviceId, date,
                biggestInterval.getCarBox().getId(), service.getPrice());
        return entryCrudService.save(entryRequestDto);
    }

    @Override
    public EntryInfoDto freeEntry(long entryId, EntryStatus status) {
        Entry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new NoSuchIdException("no such entry id: " + entryId));
        long duration = (long) (entry.getCarwashService().getDuration().toMillis() * entry.getCarBox().getTimeCoefficient());
        Date until = new Date(entry.getDate().getTime() + duration);
        intervalsService.freeInterval(entry.getDate(), until,entry.getCarBox().getId());
        return entryCrudService.updateStatus(entryId, status);
//        entryCrudService.delete(entryId);
    }

}
