package shagiev.carwash.service.availableinterval;

import shagiev.carwash.model.availableinterval.AvailableInterval;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public interface AvailableIntervalsService {

    List<AvailableInterval> getAvailableIntervalsForDay(Duration baseDuration, Date date);

    List<AvailableInterval> getIntervalsForConcreteTime(Duration baseDuration, Date date);
    void busyInterval(long id, Date from, Date until);

    void freeInterval(Date from, Date until, long carBoxId);

}
