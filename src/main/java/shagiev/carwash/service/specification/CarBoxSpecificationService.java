package shagiev.carwash.service.specification;

import org.springframework.data.jpa.domain.Specification;
import shagiev.carwash.model.carbox.CarBox;

import java.util.Date;

public interface CarBoxSpecificationService {

    Specification<CarBox> notInitialized(Date date);

}
