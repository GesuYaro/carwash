package shagiev.carwash.service.entry;

import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.EntryStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Deprecated
public interface EntryFinder {

    Map<Long, List<Entry>> findEntriesByDay(Date date, EntryStatus[] entryStatuses);

}
