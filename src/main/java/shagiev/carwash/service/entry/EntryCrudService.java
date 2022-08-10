package shagiev.carwash.service.entry;

import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryRequestDto;
import shagiev.carwash.model.entry.EntryStatus;

import java.util.List;

public interface EntryCrudService {

    List<EntryInfoDto> getAll();
    EntryInfoDto getConcrete(long id);
    EntryInfoDto save(EntryRequestDto entry);
    EntryInfoDto update(long id, EntryRequestDto entry);
    EntryInfoDto updateStatus(long id, EntryStatus status);
    void delete(long id);

    List<EntryInfoDto> getAllByCarBox(long carBoxId);

}
