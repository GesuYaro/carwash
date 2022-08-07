package shagiev.carwash.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.availableinterval.AvailableInterval;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableIntervalRepo extends JpaRepository<AvailableInterval, Long>, JpaSpecificationExecutor<AvailableInterval> {

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    List<AvailableInterval> findAll();

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    List<AvailableInterval> findAll(Specification<AvailableInterval> spec);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    Page<AvailableInterval> findAll(Specification<AvailableInterval> spec, Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    Page<AvailableInterval> findAll(Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    Optional<AvailableInterval> findById(Long aLong);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    boolean existsById(Long aLong);

    @Override
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "carBox")
    boolean exists(Specification<AvailableInterval> spec);
}
