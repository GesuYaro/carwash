package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.availableinterval.AvailableInterval;
import shagiev.carwash.model.availableinterval.AvailableInterval_;
import shagiev.carwash.model.carbox.CarBox_;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class AvailableIntervalSpecificationServiceImpl implements AvailableIntervalSpecificationService {

    @Override
    public Specification<AvailableInterval> inDay(Date date) {
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

    @Override
    public Specification<AvailableInterval> durationGreaterOrEquals(Duration duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.diff(root.get(AvailableInterval_.until), root.get(AvailableInterval_.from)), duration.toMillis());
    }

    @Override
    public Specification<AvailableInterval> fitsFromTime(Duration baseDuration, Date date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.diff(root.get(AvailableInterval_.until), date.getTime()),
                        criteriaBuilder.prod((double) baseDuration.toMillis(), root.get(AvailableInterval_.carBox).get(CarBox_.timeCoefficient)).as(Long.class)
                ),
                criteriaBuilder.lessThanOrEqualTo(root.get(AvailableInterval_.from), date.getTime())
        );
    }

    @Override
    public Specification<AvailableInterval> intervalFitsBaseDuration(Duration baseDuration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.diff(root.get(AvailableInterval_.until), root.get(AvailableInterval_.from)),
                criteriaBuilder.prod((double) baseDuration.toMillis(), root.get(AvailableInterval_.carBox).get(CarBox_.timeCoefficient)).as(Long.class)
        );
    }

    @Override
    public Specification<AvailableInterval> intersectAnotherInterval(Date from, Date until) {
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

    @Override
    public Specification<AvailableInterval> inCarBox(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(AvailableInterval_.carBox).get(CarBox_.id), id);
    }

    @Override
    public Specification<AvailableInterval> includeAnotherInterval(Date from, Date until) {
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

    @Override
    public Specification<AvailableInterval> isAdjacentInterval(Date from, Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get(AvailableInterval_.from), until.getTime()),
                criteriaBuilder.equal(root.get(AvailableInterval_.until), from.getTime())
        );
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
