package shagiev.carwash.service.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.entry.EntryStatus;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.model.user.AppUserRole;
import shagiev.carwash.model.user.OperatorInfo;
import shagiev.carwash.repo.OperatorInfoRepo;
import shagiev.carwash.service.entry.EntryCrudService;

import java.security.Principal;
import java.sql.Time;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BelongsCheckServiceImplTest {

    EntryCrudService entryCrudServiceMock = Mockito.mock(EntryCrudService.class);
    OperatorInfoRepo operatorInfoRepoMock = Mockito.mock(OperatorInfoRepo.class);
    UserFromPrincipalService userFromPrincipalServiceMock = Mockito.mock(UserFromPrincipalService.class);

    BelongsCheckServiceImpl service = new BelongsCheckServiceImpl(
            entryCrudServiceMock,
            operatorInfoRepoMock,
            userFromPrincipalServiceMock
    );

    {
        EntryInfoDto validEntry = getValidEntry();
        Mockito.when(entryCrudServiceMock.getConcrete(validEntry.getId())).thenReturn(validEntry);
        Mockito.when(userFromPrincipalServiceMock.getUser(getValidPrincipal())).thenReturn(getValidUser());
        Mockito.when(userFromPrincipalServiceMock.getUser(getInvalidPrincipal())).thenReturn(getInvalidUser());
        Mockito.when(operatorInfoRepoMock.findByUser_Id(getValidUserId())).thenReturn(getValidOperatorInfo());
        Mockito.when(operatorInfoRepoMock.findByUser_Id(getInvalidUserId())).thenReturn(getInvalidOperatorInfo());
    }

    @Test
    void isEntryBelongsToUser_validEntry_true() {
        EntryInfoDto entryInfoDto = getValidEntry();
        assertTrue(service.isEntryBelongsToUser(entryInfoDto.getId(), getValidPrincipal()));
    }

    @Test
    void isEntryBelongsToUser_invalidPrincipal_false() {
        EntryInfoDto entryInfoDto = getValidEntry();
        assertFalse(service.isEntryBelongsToUser(entryInfoDto.getId(), getInvalidPrincipal()));
    }

    @Test
    void isEntryBelongsToOperator_validEntry_validPrincipal_true() {
        EntryInfoDto entryInfoDto = getValidEntry();
        assertTrue(service.isEntryBelongsToOperator(entryInfoDto.getId(), getValidPrincipal()));
    }

    @Test
    void isEntryBelongsToOperator_validEntry_invalidPrincipal_false() {
        EntryInfoDto entryInfoDto = getValidEntry();
        assertFalse(service.isEntryBelongsToOperator(entryInfoDto.getId(), getInvalidPrincipal()));
    }

    @Test
    void isCarBoxBelongsToOperator_validCarBox_validPrincipal_true() {
        assertTrue(service.isCarBoxBelongsToOperator(getValidCarBox().getId(), getValidPrincipal()));
    }

    EntryInfoDto getValidEntry() {
        return new EntryInfoDto(1, 1, 1, 1, new Date(1000000000), EntryStatus.UNCONFIRMED, 1000);
    }

    String getValidUsername() {
        return "test";
    }

    String getInvalidUsername() {
        return "TEST11";
    }

    AppUser getValidUser() {
        return new AppUser(1, "test", "test123", AppUserRole.USER);
    }

    AppUser getInvalidUser() {
        return new AppUser(2, "TEST11", "test123", AppUserRole.USER);
    }

    Principal getValidPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return getValidUsername();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                return ((Principal) obj).getName().equals(this.getName());
            }
        };
    }

    long getValidUserId() {
        return 1;
    }

    long getInvalidUserId() {
        return 20;
    }

    Principal getInvalidPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return getInvalidUsername();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                return ((Principal) obj).getName().equals(this.getName());
            }
        };
    }

    CarBox getValidCarBox() {
        return new CarBox(1, new Time(9, 0, 0), new Time(19, 0, 0), 1.0);
    }

    CarBox getInvalidCarBox() {
        return new CarBox(2, new Time(9, 0, 0), new Time(19, 0, 0), 1.0);
    }

    OperatorInfo getValidOperatorInfo() {
        return new OperatorInfo(1, getValidUser(), getValidCarBox(), 0.1, 0.15);
    }

    OperatorInfo getInvalidOperatorInfo() {
        return new OperatorInfo(2, getInvalidUser(), getInvalidCarBox(), 0.1, 0.15);
    }
}