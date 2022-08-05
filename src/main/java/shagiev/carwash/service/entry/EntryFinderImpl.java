package shagiev.carwash.service.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.model.entry.Entry_;
import shagiev.carwash.repo.EntryRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EntryFinderImpl implements EntryFinder {

    private final EntryRepo entryRepo;

    @Override
    public Map<Long, List<Entry>> findEntriesByDay(Date date, EntryStatus[] entryStatuses) {
        List<Entry> entries = entryRepo.findAll(getSpecification(date, entryStatuses));
        Map<Long, List<Entry>> map = new HashMap<>();
        for (Entry entry: entries) {
            long carBoxId = entry.getCarBox().getId();
            if (!map.containsKey(carBoxId)) {
                map.put(carBoxId, new ArrayList<>());
            }
            map.get(carBoxId).add(entry);
        }
        return map;
    }

    private Specification<Entry> getSpecification(Date date, EntryStatus[] entryStatuses) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.and(getPredicates(date, entryStatuses, root, criteriaBuilder));
    }

    private Predicate[] getPredicates(Date date, EntryStatus[] entryStatuses, Root<Entry> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> statusPredicates = new ArrayList<>();
        for (EntryStatus entryStatus: entryStatuses) {
            statusPredicates.add(criteriaBuilder.equal(root.get(Entry_.status), entryStatus));
        }
        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.or(statusPredicates.toArray(Predicate[]::new));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        java.util.Date endDate = calendar.getTime();
        predicates[1] = criteriaBuilder.between(root.get(Entry_.date), date, endDate);
        return predicates;
    }

}
