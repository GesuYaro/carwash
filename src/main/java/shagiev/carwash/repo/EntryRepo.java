package shagiev.carwash.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.entry.Entry;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long>, JpaSpecificationExecutor<Entry> {

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"carBox", "carwashService"})
    List<Entry> findAll();

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"carBox", "carwashService"})
    List<Entry> findAll(Specification<Entry> spec);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"carBox", "carwashService"})
    Page<Entry> findAll(Specification<Entry> spec, Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"carBox", "carwashService"})
    Page<Entry> findAll(Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"carBox", "carwashService"})
    Optional<Entry> findById(Long aLong);

}
