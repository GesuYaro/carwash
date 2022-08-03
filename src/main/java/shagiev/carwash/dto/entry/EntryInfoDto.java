package shagiev.carwash.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import shagiev.carwash.model.entry.EntryStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class EntryInfoDto {

    private long id;
    private long carboxId;
    private long serviceId;
    private Date date;
    private EntryStatus status;
    private long price;

}
