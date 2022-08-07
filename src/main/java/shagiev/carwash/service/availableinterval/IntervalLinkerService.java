package shagiev.carwash.service.availableinterval;

import shagiev.carwash.dto.availableinterval.AvailableIntervalDto;
import shagiev.carwash.model.availableinterval.AvailableInterval;

import java.util.List;

public interface IntervalLinkerService {

    List<AvailableIntervalDto> compose(List<AvailableInterval> availableIntervals);

}
