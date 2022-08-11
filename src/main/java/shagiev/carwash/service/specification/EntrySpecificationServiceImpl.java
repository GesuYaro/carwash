package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.carbox.CarBox_;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.Entry_;

import java.util.Date;

public class EntrySpecificationServiceImpl implements EntrySpecificationService {

    @Override
    public Specification<Entry> inCarBox(long carBoxId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(Entry_.carBox).get(CarBox_.id),
                carBoxId
        );
    }

    @Override
    public Specification<Entry> afterDate(Date date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(Entry_.date),
                date
        );
    }

    @Override
    public Specification<Entry> beforeDate(Date date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(Entry_.date),
                date
        );
    }

}
