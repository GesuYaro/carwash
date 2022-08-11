package shagiev.carwash.dto.user.operator;

import lombok.Data;

@Data
public class OperatorInfoDto {

    private long id;
    private long userId;
    private long carboxId;
    private double minSale;
    private double maxSale;

}
