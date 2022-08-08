package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.availableinterval.AvailableInterval;

import java.time.Duration;
import java.util.Date;

public interface AvailableIntervalSpecificationService {

    Specification<AvailableInterval> inDay(Date date);
    Specification<AvailableInterval> durationGreaterOrEquals(Duration duration);
    Specification<AvailableInterval> fitsFromTime(Duration baseDuration, Date date);
    Specification<AvailableInterval> intervalFitsBaseDuration(Duration baseDuration);
    Specification<AvailableInterval> intersectAnotherInterval(Date from, Date until);
    Specification<AvailableInterval> inCarBox(long id);
    Specification<AvailableInterval> includeAnotherInterval(Date from, Date until);
    Specification<AvailableInterval> isAdjacentInterval(Date from, Date until);


}
