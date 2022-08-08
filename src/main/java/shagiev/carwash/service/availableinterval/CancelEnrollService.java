package shagiev.carwash.service.availableinterval;

import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.EntryStatus;

public interface CancelEnrollService {

    EntryInfoDto freeEntry(long entryId, EntryStatus status);

}
