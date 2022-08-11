package shagiev.carwash.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@AllArgsConstructor
public class CarwashServiceRequestDto {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Duration duration;

    @Min(1)
    private long price;

}
