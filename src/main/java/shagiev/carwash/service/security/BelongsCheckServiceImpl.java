package shagiev.carwash.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.model.user.OperatorInfo;
import shagiev.carwash.repo.OperatorInfoRepo;
import shagiev.carwash.service.entry.EntryCrudService;
import shagiev.carwash.service.exceptions.NoSuchIdException;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class BelongsCheckServiceImpl implements BelongsCheckService {

    private final EntryCrudService entryCrudService;
    private final OperatorInfoRepo operatorInfoRepo;
    private final UserFromPrincipalService userFromPrincipalService;

    @Override
    public boolean isEntryBelongsToUser(Long entryId, Principal principal) {
        if (entryId == null) {
            return false;
        }
        try {
            AppUser user = userFromPrincipalService.getUser(principal);
            if (user == null) {
                return false;
            }
            EntryInfoDto entryInfoDto = entryCrudService.getConcrete(entryId);
            return entryInfoDto.getUserId() == user.getId();
        } catch (NoSuchIdException e) {
            return false;
        }
    }

    @Override
    public boolean isEntryBelongsToOperator(Long entryId, Principal principal) {
        if (entryId == null) {
            return false;
        }
        try {
            AppUser user = userFromPrincipalService.getUser(principal);
            if (user == null) {
                return false;
            }
            EntryInfoDto entryInfoDto = entryCrudService.getConcrete(entryId);
            OperatorInfo operatorInfo = operatorInfoRepo.findByUser_Id(user.getId());
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
    public boolean isCarBoxBelongsToOperator(Long carboxId, Principal principal) {
        if (carboxId == null) {
            return false;
        }
        AppUser user = userFromPrincipalService.getUser(principal);
        if (user == null) {
            return false;
        }
        OperatorInfo operatorInfo = operatorInfoRepo.findByUser_Id(user.getId());
        if (operatorInfo == null) {
            return false;
        }
        if (operatorInfo.getCarBox() == null) {
            return false;
        }
        return operatorInfo.getCarBox().getId() == carboxId;
    }

}
