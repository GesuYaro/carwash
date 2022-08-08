package shagiev.carwash.service.availableinterval;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.availableinterval.AvailableInterval;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.repo.AvailableIntervalRepo;
import shagiev.carwash.repo.CarBoxRepo;
import shagiev.carwash.service.exceptions.NoSuchIdException;
import shagiev.carwash.service.specification.AvailableIntervalSpecificationService;
import shagiev.carwash.service.specification.CarBoxSpecificationService;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableIntervalsServiceImpl implements AvailableIntervalsService {

    private final AvailableIntervalRepo availableIntervalRepo;
    private final CarBoxRepo carBoxRepo;
    private final AvailableIntervalSpecificationService intervalSpecificationService;
    private final CarBoxSpecificationService carBoxSpecificationService;

    @Override
    public List<AvailableInterval> getAvailableIntervalsForDay(Duration baseDuration, Date date) {
        initNewDay(date);
        return availableIntervalRepo.findAll(inDay(date).and(intervalFitsBaseDuration(baseDuration)));
    }

    private void initNewDay(Date date) {
        List<CarBox> carBoxes = carBoxRepo.findAll(notInitialized(date));
        if (!carBoxes.isEmpty()) {
            Calendar requestCalendar = Calendar.getInstance();
            requestCalendar.setTime(date);
            for (CarBox carBox : carBoxes) {
                Calendar opening = Calendar.getInstance();
                opening.setTime(carBox.getOpeningTime());
                opening.set(Calendar.YEAR, requestCalendar.get(Calendar.YEAR));
                opening.set(Calendar.MONTH, requestCalendar.get(Calendar.MONTH));
                opening.set(Calendar.DAY_OF_MONTH, requestCalendar.get(Calendar.DAY_OF_MONTH));

                Calendar closing = Calendar.getInstance();
                closing.setTime(carBox.getClosingTime());
                closing.set(Calendar.YEAR, requestCalendar.get(Calendar.YEAR));
                closing.set(Calendar.MONTH, requestCalendar.get(Calendar.MONTH));
                closing.set(Calendar.DAY_OF_MONTH, requestCalendar.get(Calendar.DAY_OF_MONTH));

                freeInterval(opening.getTime(), closing.getTime(), carBox.getId());
            }
        }
    }

    private Specification<CarBox> notInitialized(Date date) {
        return carBoxSpecificationService.notInitialized(date);
    }

    @Override
    public List<AvailableInterval> getIntervalsForConcreteTime(Duration baseDuration, Date date) {
        initNewDay(date);
        return availableIntervalRepo.findAll(fitsFromTime(baseDuration, date));
    }

    @Override
    @Transactional
    public void busyInterval(long id, Date from, Date until) {
        if (!availableIntervalRepo.existsById(id)) {
            throw new NoSuchIdException("No such interval id");
        }
        AvailableInterval interval = availableIntervalRepo.findById(id)
                .orElseThrow(() -> new NoSuchIdException("No such interval id"));
        if (interval.getFrom() > from.getTime()) {
            throw new IllegalArgumentException("\"from\" argument if out of interval borders");
        }
        if (interval.getUntil() < until.getTime()) {
            throw new IllegalArgumentException("\"until\" argument if out of interval borders");
        }

        if (interval.getFrom() < from.getTime()) {
            AvailableInterval beforeInterval = new AvailableInterval(
                    0,
                    interval.getCarBox(),
                    interval.getFrom(),
                    from.getTime());
            availableIntervalRepo.save(beforeInterval);
        }

        if (interval.getUntil() > until.getTime()) {
            AvailableInterval afterInterval = new AvailableInterval(
                    0,
                    interval.getCarBox(),
                    until.getTime(),
                    interval.getUntil());
            availableIntervalRepo.save(afterInterval);
        }

        availableIntervalRepo.delete(interval);
    }

    @Override
    @Transactional
    public void freeInterval(Date from, Date until, long carBoxId) {
        if (!carBoxRepo.existsById(carBoxId)) {
            throw new NoSuchIdException("no such carBoxId");
        }
        if (availableIntervalRepo.exists(intersectAnotherInterval(from, until)
                .and(inCarBox(carBoxId)))
                || availableIntervalRepo.exists(includeAnotherInterval(from, until)
                .and(inCarBox(carBoxId)))) {
            throw new IllegalArgumentException("interval can't intersect or include other intervals");
        }
        if (availableIntervalRepo.exists(
                isAdjacentInterval(from, until)
                        .and(inCarBox(carBoxId))
        )) {
            List<AvailableInterval> adjacentIntervals = availableIntervalRepo.findAll(
                    isAdjacentInterval(from, until)
                            .and(inCarBox(carBoxId))
            );
            CarBox carBox = carBoxRepo.findById(carBoxId).orElseThrow(() -> new NoSuchIdException("no such carBoxId"));
            if (adjacentIntervals.isEmpty()) {
                availableIntervalRepo.save(new AvailableInterval(0, carBox, from.getTime(), until.getTime()));
            } else {
                long minFrom = adjacentIntervals.stream()
                        .mapToLong(AvailableInterval::getFrom)
                        .min().getAsLong();
                long maxUntil = adjacentIntervals.stream()
                        .mapToLong(AvailableInterval::getUntil)
                        .max().getAsLong();
                AvailableInterval bigInterval = new AvailableInterval(0, carBox, minFrom, maxUntil);
                availableIntervalRepo.save(bigInterval);
                availableIntervalRepo.deleteAll(adjacentIntervals);
            }
        } else {
            CarBox carBox = carBoxRepo.findById(carBoxId).orElseThrow(() -> new NoSuchIdException("no such carBoxId"));
            availableIntervalRepo.save(new AvailableInterval(0, carBox, from.getTime(), until.getTime()));
        }
    }

    private Specification<AvailableInterval> inDay(Date date) {
        return intervalSpecificationService.inDay(date);
    }

    private Specification<AvailableInterval> durationGreaterOrEquals(Duration duration) {
        return intervalSpecificationService.durationGreaterOrEquals(duration);
    }

    private Specification<AvailableInterval> fitsFromTime(Duration baseDuration, Date date) {
        return intervalSpecificationService.fitsFromTime(baseDuration, date);
    }

    private Specification<AvailableInterval> intervalFitsBaseDuration(Duration baseDuration) {
        return intervalSpecificationService.intervalFitsBaseDuration(baseDuration);
    }

    private Specification<AvailableInterval> intersectAnotherInterval(Date from, Date until) {
        return intervalSpecificationService.intersectAnotherInterval(from, until);
    }

    private Specification<AvailableInterval> inCarBox(long id) {
        return intervalSpecificationService.inCarBox(id);
    }

    private Specification<AvailableInterval> includeAnotherInterval(Date from, Date until) {
        return intervalSpecificationService.includeAnotherInterval(from, until);
    }

    private Specification<AvailableInterval> isAdjacentInterval(Date from, Date until) {
        return intervalSpecificationService.isAdjacentInterval(from, until);
    }

}
