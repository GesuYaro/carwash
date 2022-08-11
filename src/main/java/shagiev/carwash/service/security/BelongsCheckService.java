package shagiev.carwash.service.security;

import java.security.Principal;

public interface BelongsCheckService {

    boolean isEntryBelongsToUser(Long entryId, Principal principal);
    boolean isEntryBelongsToOperator(Long entryId, Principal principal);
    boolean isCarBoxBelongsToOperator(Long carboxId, Principal principal);

}
