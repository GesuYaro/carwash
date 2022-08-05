package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shagiev.carwash.model.carbox.CarBox;

@Repository
public interface CarBoxRepo extends JpaRepository<CarBox, Long>, JpaSpecificationExecutor<CarBox> {
}
