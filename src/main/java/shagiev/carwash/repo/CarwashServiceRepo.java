package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shagiev.carwash.model.service.CarwashService;

@Repository
public interface CarwashServiceRepo extends JpaRepository<CarwashService, Long>, JpaSpecificationExecutor<CarwashService> {
}
