package shagiev.carwash.service.availableinterval;

import org.springframework.stereotype.Service;
import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.model.availableinterval.AvailableInterval;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
public class IntervalLinkerServiceImpl implements IntervalLinkerService {

    @Override
    public List<AvailableIntervalDto> compose(List<AvailableInterval> availableIntervals, Duration baseDuration) {
        List<AvailableIntervalDto> composed = new ArrayList<>();
        availableIntervals.forEach(interval -> {
            long duration = (long) (interval.getCarBox().getTimeCoefficient() * baseDuration.toMillis());
            interval.setUntil(interval.getUntil() - duration);
        });
        availableIntervals.sort(
                Comparator.comparing(AvailableInterval::getFrom)
                .thenComparing((i1, i2) -> Long.compare(i2.getUntil(), i1.getUntil()))
        );
        Iterator<AvailableInterval> iterator = availableIntervals.iterator();
        if (iterator.hasNext()) {
            AvailableInterval prevInterval = iterator.next();
            AvailableIntervalDto previous = new AvailableIntervalDto(
                    new Time(prevInterval.getFrom()), new Time(prevInterval.getUntil()));
            if (!iterator.hasNext()) {
                composed.add(previous);
            }
            while (iterator.hasNext()) {
                AvailableInterval current = iterator.next();
                if (previous.getUntil().getTime() >= current.getFrom()) {
                    if (previous.getUntil().getTime() < current.getUntil()) {
                        previous.setUntil(new Time(current.getUntil()));
                    }
                } else {
                    composed.add(previous);
                    previous = new AvailableIntervalDto(
                            new Time(current.getFrom()), new Time(current.getUntil()));
                }
                if (!iterator.hasNext()) {
                    composed.add(previous);
                }
            }
        }
        return composed;
    }

}

