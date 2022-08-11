package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.entry.Entry;

import java.util.Date;

public interface EntrySpecificationService {

    Specification<Entry> inCarBox(long carBoxId);
    Specification<Entry> afterDate(Date date);
    Specification<Entry> beforeDate(Date date);

}
