package shagiev.carwash.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
public class EntryUserRequestDto {

    @Min(1)
    private long serviceId;

    @NotNull
    private Date date;

}
