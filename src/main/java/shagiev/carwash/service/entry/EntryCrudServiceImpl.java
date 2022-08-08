package shagiev.carwash.service.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.dto.entry.EntryRequestDto;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.entry.Entry;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.model.service.CarwashService;
import shagiev.carwash.repo.CarBoxRepo;
import shagiev.carwash.repo.CarwashServiceRepo;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryCrudServiceImpl implements EntryCrudService {

    private final EntryRepo entryRepo;
    private final CarBoxRepo carBoxRepo;
    private final CarwashServiceRepo carwashServiceRepo;
    private final ConversionService conversionService;

    @Override
    public List<EntryInfoDto> getAll() {
        List<EntryInfoDto> dtos = new ArrayList<>();
        entryRepo.findAll().forEach(entry ->
                dtos.add(conversionService.convert(entry, EntryInfoDto.class))
        );
        return dtos;
    }

    @Override
    public EntryInfoDto getConcrete(long id) {
        var entry = entryRepo.findById(id);
        if (entry.isPresent()) {
            return conversionService.convert(entry.get(), EntryInfoDto.class);
        } else {
            return null;
        }
    }

    @Override
    public EntryInfoDto save(EntryRequestDto entryDto) {
        CarBox carBox = carBoxRepo.findById(entryDto.getCarboxId())
                .orElseThrow(() -> new NoSuchIdException("no such carbox (id = " + entryDto.getCarboxId() + ")"));
        CarwashService service = carwashServiceRepo.findById(entryDto.getServiceId())
                .orElseThrow(() -> new NoSuchIdException("no such service (id = " + entryDto.getServiceId() + ")"));
        Entry entry = new Entry(
                0,
                carBox,
                service,
                entryDto.getDate(),
                EntryStatus.UNCONFIRMED,
                entryDto.getPrice()
        );
        Entry savedEntry = entryRepo.save(entry);
        return conversionService.convert(savedEntry, EntryInfoDto.class);
    }

    @Override
    public EntryInfoDto update(long id, EntryRequestDto entryDto) {
        CarBox carBox = carBoxRepo.findById(entryDto.getCarboxId())
                .orElseThrow(() -> new NoSuchIdException("no such carbox (id = " + entryDto.getCarboxId() + ")"));
        CarwashService service = carwashServiceRepo.findById(entryDto.getServiceId())
                .orElseThrow(() -> new NoSuchIdException("no such service (id = " + entryDto.getServiceId() + ")"));
        Entry entry = new Entry(
                id,
                carBox,
                service,
                entryDto.getDate(),
                EntryStatus.UNCONFIRMED,
                entryDto.getPrice()
        );
        if (entryRepo.existsById(id)) {
            Entry savedEntry = entryRepo.save(entry);
            return conversionService.convert(savedEntry, EntryInfoDto.class);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        entryRepo.deleteById(id);
    }

    @Override
    public EntryInfoDto updateStatus(long id, EntryStatus status) {
        Entry oldEntry = entryRepo.findById(id)
                .orElseThrow(() -> new NoSuchIdException("no such entry (id = " + id + ")"));
        Entry entry = new Entry(
                id,
                oldEntry.getCarBox(),
                oldEntry.getCarwashService(),
                oldEntry.getDate(),
                status,
                oldEntry.getPrice()
        );
        if (entryRepo.existsById(id)) {
            Entry savedEntry = entryRepo.save(entry);
            return conversionService.convert(savedEntry, EntryInfoDto.class);
        }
        return null;
    }
}
