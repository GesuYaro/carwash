package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import shagiev.carwash.model.availableinterval.AvailableInterval;

public interface AvailableIntervalRepo extends JpaRepository<AvailableInterval, Long>, JpaSpecificationExecutor<AvailableInterval> {
}
