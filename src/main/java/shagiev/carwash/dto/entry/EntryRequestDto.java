package shagiev.carwash.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
public class EntryRequestDto {

    @Min(1)
    private long serviceId;

    @Min(1)
    private long userId;

    @NotNull
    private Date date;

    @Min(1)
    private long carboxId;

    @Min(1)
    private long price;

}
