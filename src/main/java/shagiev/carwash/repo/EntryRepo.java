package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shagiev.carwash.model.entry.Entry;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long>, JpaSpecificationExecutor<Entry> {
}
