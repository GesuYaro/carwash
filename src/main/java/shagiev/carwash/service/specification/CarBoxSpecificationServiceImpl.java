package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.availableinterval.AvailableInterval;
import shagiev.carwash.model.availableinterval.AvailableInterval_;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.carbox.CarBox_;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.Entry_;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Calendar;
import java.util.Date;

public class CarBoxSpecificationServiceImpl implements CarBoxSpecificationService {

    @Override
    public Specification<CarBox> notInitialized(Date date) {
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
