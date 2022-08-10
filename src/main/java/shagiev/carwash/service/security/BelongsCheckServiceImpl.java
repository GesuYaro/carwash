package shagiev.carwash.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.user.OperatorInfo;
import shagiev.carwash.repo.OperatorInfoRepo;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

@Service
@RequiredArgsConstructor
public class BelongsCheckServiceImpl implements BelongsCheckService {

    private final EntryCrudService entryCrudService;
    private final OperatorInfoRepo operatorInfoRepo;

    @Override
    public boolean isEntryBelongsToUser(long entryId, long userId) {
        try {
            EntryInfoDto entryInfoDto = entryCrudService.getConcrete(entryId);
            return entryInfoDto.getUserId() == userId;
        } catch (NoSuchIdException e) {
            return false;
        }
    }

    @Override
    public boolean isEntryBelongsToOperator(long entryId, long operatorId) {
        try {
            EntryInfoDto entryInfoDto = entryCrudService.getConcrete(entryId);
            OperatorInfo operatorInfo = operatorInfoRepo.findByUser_Id(operatorId);
            if (operatorInfo == null) {
                return false;
            }
            if (operatorInfo.getCarBox() == null) {
                return false;
            }
            return operatorInfo.getCarBox().getId() == entryInfoDto.getCarboxId();
        } catch (NoSuchIdException e) {
            return false;
        }
    }

    @Override
    public boolean isCarBoxBelongsToOperator(long carboxId, long operatorId) {
        OperatorInfo operatorInfo = operatorInfoRepo.findByUser_Id(operatorId);
        if (operatorInfo == null) {
            return false;
        }
        if (operatorInfo.getCarBox() == null) {
            return false;
        }
        return operatorInfo.getCarBox().getId() == carboxId;
    }

}
