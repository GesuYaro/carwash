package shagiev.carwash.service.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.repo.EntryRepo;
import shagiev.carwash.service.util.DateParserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final EntryRepo entryRepo;
    private final DateParserService dateParserService;

    @Override
    public Long countIncome(String from, String until) {
        return entryRepo.sumIncome(
                dateParserService.getDate(from),
                dateParserService.getDate(until)
        );
    }


}
