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
import shagiev.carwash.service.entry.EntryConfirmationService;
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
    private final EntryConfirmationService entryConfirmationService;
    private final CancelEnrollService cancelEnrollService;

    @Override
    public List<AvailableIntervalDto> getAvailableIntervals(Date date, long serviceId) {
        Duration baseDuration = carwashServiceCrudService.getConcrete(serviceId).getDuration();
        return intervalLinkerService.compose(intervalsService.getAvailableIntervalsForDay(baseDuration, date), baseDuration);
    }

    @Override
    public EntryInfoDto makeEntry(Date date, long serviceId, long userId) {
        if (!carwashServiceRepo.existsById(serviceId)) {
            throw new NoSuchIdException("no such service");
        }
        Duration baseDuration = carwashServiceCrudService.getConcrete(serviceId).getDuration();
        List<AvailableInterval> intervals = intervalsService.getIntervalsForConcreteTime(baseDuration, date);
        if (intervals.isEmpty()) {
            throw new IllegalArgumentException("No interval for entry");
        }
        AvailableInterval biggestInterval = intervals.stream()
                .max(Comparator.comparing((i) -> i.getUntil() - i.getFrom()))
                .orElseThrow(() -> new IllegalArgumentException("No interval for entry"));
        Date until = new Date(date.getTime()
                + (long) (baseDuration.toMillis() * biggestInterval.getCarBox().getTimeCoefficient()));
        intervalsService.busyInterval(biggestInterval.getId(), date, until);
        CarwashService service = carwashServiceRepo.getReferenceById(serviceId);

        EntryRequestDto entryRequestDto = new EntryRequestDto(serviceId, userId, date,
                biggestInterval.getCarBox().getId(), service.getPrice());
        EntryInfoDto savedEntry = entryCrudService.save(entryRequestDto);
        entryConfirmationService.startConfirmation(savedEntry);
        return savedEntry;
    }

    @Override
    public EntryInfoDto freeEntry(long entryId, EntryStatus status, long userId) {
        return cancelEnrollService.freeEntry(entryId, status);
    }

    @Override
    public EntryInfoDto confirm(long entryId, long userId) {
        Entry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new NoSuchIdException("no such entry id: " + entryId));
        if (entry.getStatus() == EntryStatus.UNCONFIRMED) {
            return entryCrudService.updateStatus(entryId, EntryStatus.CONFIRMED);
        } else {
            throw new IllegalArgumentException("Can't confirm entry");
        }
    }
}
