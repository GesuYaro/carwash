package shagiev.carwash.service.entry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.service.availableinterval.CancelEnrollService;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntryConfirmationServiceImpl implements EntryConfirmationService {

    @Value("${carwash.confirmation.confirm-timer}")
    private long confirmationTimeoutInMillis;

    private final ConfirmationInfoSenderService confirmationInfoSenderService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final CancelEnrollService enrollService;
    private final EntryCrudService entryCrudService;

    @Override
    public void startConfirmation(EntryInfoDto entryInfoDto) {
        confirmationInfoSenderService.sendConfirmationInfo(entryInfoDto);
        Date dateOfCanceling = new Date(System.currentTimeMillis() + confirmationTimeoutInMillis);
        taskScheduler.schedule(() -> cancelEntryIfNotConfirmed(entryInfoDto), dateOfCanceling);
    }

    private void cancelEntryIfNotConfirmed(EntryInfoDto entryInfoDto) {
        EntryInfoDto inBaseEntry = entryCrudService.getConcrete(entryInfoDto.getId());
        if (inBaseEntry != null) {
            if (inBaseEntry.getStatus() == EntryStatus.UNCONFIRMED) {
                log.info("Cancelling entry (id = {}). Confirmation timeout exceeded", inBaseEntry.getId());
                enrollService.freeEntry(inBaseEntry.getId(), EntryStatus.CANCELED);
                log.info("Entry (id = {}) successfully canceled", inBaseEntry.getId());
            } else {
                log.info("Can't cancel entry (id = {}). Entry status isn't {} ({} != {})",
                        entryInfoDto.getId(), EntryStatus.UNCONFIRMED.name(),
                        inBaseEntry.getStatus().name(), EntryStatus.UNCONFIRMED.name());
            }
        } else {
            log.info("Can't cancel entry (id = {}). Entry not in database", entryInfoDto.getId());
        }
    }
}
