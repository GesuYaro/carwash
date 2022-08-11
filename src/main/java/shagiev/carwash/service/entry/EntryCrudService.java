package shagiev.carwash.service.entry;

import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryRequestDto;
import shagiev.carwash.model.entry.EntryStatus;

import java.util.Date;
import java.util.List;

public interface EntryCrudService {

    List<EntryInfoDto> getAll();
    List<EntryInfoDto> getAll(Long carBoxId, Date from, Date until, Integer page, Integer countOnPage);
    EntryInfoDto getConcrete(long id);
    EntryInfoDto save(EntryRequestDto entry);
    EntryInfoDto update(long id, EntryRequestDto entry);
    EntryInfoDto updateStatus(long id, EntryStatus status);
    void delete(long id);

    List<EntryInfoDto> getAllByCarBox(long carBoxId);

}
