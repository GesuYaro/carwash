package shagiev.carwash.service.availableinterval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CancelEnrollServiceImpl implements CancelEnrollService {

    private final EntryRepo entryRepo;
    private final AvailableIntervalsService intervalsService;
    private final EntryCrudService entryCrudService;


    @Override
    public EntryInfoDto freeEntry(long entryId, EntryStatus status) {
        Entry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new NoSuchIdException("no such entry id: " + entryId));
        if (entry.getStatus() == EntryStatus.CONFIRMED || entry.getStatus() == EntryStatus.UNCONFIRMED) {
            long duration = (long) (entry.getCarwashService().getDuration().toMillis() * entry.getCarBox().getTimeCoefficient());
            Date until = new Date(entry.getDate().getTime() + duration);
            intervalsService.freeInterval(entry.getDate(), until, entry.getCarBox().getId());
            return entryCrudService.updateStatus(entryId, status);
        } else {
            return null;
        }
    }
}
