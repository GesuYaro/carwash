package shagiev.carwash.service.entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;

@Service
public class ConfirmationInfoConsoleSenderService implements ConfirmationInfoSenderService {

    @Value("${carwash.confirmation.confirm-address}")
    private String confirmAddress;

    @Override
    public void sendConfirmationInfo(EntryInfoDto entryInfoDto) {
        System.out.println(confirmAddress + "/" + entryInfoDto.getId());
    }

}
