package shagiev.carwash.dto.user.operator;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class OperatorRequestDto {

    @Min(1)
    private long userId;

    @Min(1)
    private long carboxId;

    @Min(0)
    @Max(1)
    private double minSale;

    @Min(0)
    @Max(1)
    private double maxSale;

}
