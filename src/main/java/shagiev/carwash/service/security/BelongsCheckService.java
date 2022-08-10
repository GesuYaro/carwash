package shagiev.carwash.service.security;

public interface BelongsCheckService {

    boolean isEntryBelongsToUser(long entryId, long userId);
    boolean isEntryBelongsToOperator(long entryId, long operatorId);
    boolean isCarBoxBelongsToOperator(long carboxId, long operatorId);

}
