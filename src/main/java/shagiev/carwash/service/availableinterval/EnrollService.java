package shagiev.carwash.service.availableinterval;

import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.EntryStatus;

import java.util.Date;
import java.util.List;

public interface EnrollService {

    List<AvailableIntervalDto> getAvailableIntervals(Date date, long serviceId);
    EntryInfoDto makeEntry(Date date, long serviceId, long userId);
    EntryInfoDto freeEntry(long entryId, EntryStatus status, long userId);
    EntryInfoDto confirm(long entryId, long userId);

}
