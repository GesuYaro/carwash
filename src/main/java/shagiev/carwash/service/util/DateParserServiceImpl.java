package shagiev.carwash.service.util;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateParserServiceImpl implements DateParserService {

    @Override
    public Date getDate(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException("wrong date format");
        }
        return date;
    }
}
