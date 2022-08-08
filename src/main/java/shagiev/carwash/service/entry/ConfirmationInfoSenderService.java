package shagiev.carwash.service.entry;

import shagiev.carwash.dto.entry.EntryInfoDto;

public interface ConfirmationInfoSenderService {

    void sendConfirmationInfo(EntryInfoDto entryInfoDto);

}
