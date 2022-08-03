package shagiev.carwash.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class CarwashServiceRequestDto {

    private String name;
    private Duration duration;
    private long price;

}
