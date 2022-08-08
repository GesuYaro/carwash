package shagiev.carwash.service.availableinterval;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.availableinterval.AvailableInterval;
import shagiev.carwash.model.availableinterval.AvailableInterval_;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.carbox.CarBox_;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.Entry_;
import shagiev.carwash.repo.AvailableIntervalRepo;
import shagiev.carwash.repo.CarBoxRepo;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableIntervalsServiceImpl implements AvailableIntervalsService {

    private final AvailableIntervalRepo availableIntervalRepo;
    private final EntryRepo entryRepo;
    private final CarBoxRepo carBoxRepo;

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
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> intervalCheckSubquery = query.subquery(Long.class);
            Subquery<Long> entryCheckSubquery = query.subquery(Long.class);
            Root<Entry> entryRoot = entryCheckSubquery.from(Entry.class);
            Join<Entry, CarBox> entryCarBoxJoin = entryRoot.join(Entry_.carBox);
            Root<AvailableInterval> intervalRoot = intervalCheckSubquery.from(AvailableInterval.class);
            Join<AvailableInterval, CarBox> intervalCarBoxJoin = intervalRoot.join(AvailableInterval_.carBox);

            Date until = getEndOfDay(date);
            Date from = getBeginningOfDay(date);
            intervalCheckSubquery.select(intervalCarBoxJoin.get(CarBox_.id))
                    .where(criteriaBuilder.between(
                            intervalRoot.get(AvailableInterval_.from),
                            from.getTime(),
                            until.getTime()
                    ));
            entryCheckSubquery.select(entryCarBoxJoin.get(CarBox_.id))
                    .where(criteriaBuilder.between(
                            entryRoot.get(Entry_.date),
                            from,
                            until
                    ));
            return criteriaBuilder.and(
                    criteriaBuilder.in(root.get(CarBox_.id)).value(intervalCheckSubquery).not(),
                    criteriaBuilder.in(root.get(CarBox_.id)).value(entryCheckSubquery).not()
            );
        };
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
        return (root, query, criteriaBuilder) -> {

            Date until = getEndOfDay(date);
            Calendar now = Calendar.getInstance();
            Calendar requested = Calendar.getInstance();
            requested.setTime(date);

            if (now.get(Calendar.DATE) == requested.get(Calendar.DATE)) {
                Date from = now.getTime();
                return criteriaBuilder.between(root.get(AvailableInterval_.from), from.getTime(), until.getTime());
            } else {
                Date from = getBeginningOfDay(date);
                return criteriaBuilder.between(root.get(AvailableInterval_.from), from.getTime(), until.getTime());
            }
        };
    }

    private Specification<AvailableInterval> durationGreaterOrEquals(Duration duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.diff(root.get(AvailableInterval_.until), root.get(AvailableInterval_.from)), duration.toMillis());
    }

    private Specification<AvailableInterval> fitsFromTime(Duration baseDuration, Date date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.diff(root.get(AvailableInterval_.until), date.getTime()),
                    criteriaBuilder.prod((double) baseDuration.toMillis(), root.get(AvailableInterval_.carBox).get(CarBox_.timeCoefficient)).as(Long.class)
                ),
                criteriaBuilder.lessThanOrEqualTo(root.get(AvailableInterval_.from), date.getTime())
        );
    }

    private Specification<AvailableInterval> intervalFitsBaseDuration(Duration baseDuration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.diff(root.get(AvailableInterval_.until), root.get(AvailableInterval_.from)),
                criteriaBuilder.prod((double) baseDuration.toMillis(), root.get(AvailableInterval_.carBox).get(CarBox_.timeCoefficient)).as(Long.class)
        );
    }

    private Specification<AvailableInterval> intersectAnotherInterval(Date from, Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get(AvailableInterval_.from), from.getTime()),
                        criteriaBuilder.lessThan(root.get(AvailableInterval_.until), from.getTime())
                ),
                criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get(AvailableInterval_.from), until.getTime()),
                        criteriaBuilder.lessThan(root.get(AvailableInterval_.until), until.getTime())
                )
        );
    }

    private Specification<AvailableInterval> inCarBox(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(AvailableInterval_.carBox).get(CarBox_.id), id);
    }

    private Specification<AvailableInterval> includeAnotherInterval(Date from, Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get(AvailableInterval_.from), from.getTime()),
                        criteriaBuilder.lessThanOrEqualTo(root.get(AvailableInterval_.until), until.getTime())
                ),
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(AvailableInterval_.from), from.getTime()),
                        criteriaBuilder.lessThan(root.get(AvailableInterval_.until), until.getTime())
                )
        );
    }

    private Specification<AvailableInterval> isAdjacentInterval(Date from, Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get(AvailableInterval_.from), until.getTime()),
                criteriaBuilder.equal(root.get(AvailableInterval_.until), from.getTime())
        );
    }

    private Specification<Entry> entryInDay(Date date) {
        Date from = getBeginningOfDay(date);
        Date until = getEndOfDay(date);
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Entry_.date), from, until);
    }

    private Date getBeginningOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
