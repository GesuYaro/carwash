package shagiev.carwash.service.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.util.DateParserService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final EntryRepo entryRepo;
    private final DateParserService dateParserService;

    @Override
    public Long countIncome(String from, String until) {
        Date dateFrom = dateParserService.getDate(from);
        Date dateUntil = dateParserService.getDate(until);
        if (dateFrom == null) {
            dateFrom = new Date(0);
        }
        if (dateUntil == null) {
            dateUntil = new Date(Long.MAX_VALUE / 2048);
        }
        return entryRepo.sumIncome(
                dateFrom,
                dateUntil
        );
    }


}
