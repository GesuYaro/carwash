package shagiev.carwash.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.dto.user.AppUserDto;
import shagiev.carwash.dto.user.AppUserRequestDto;
import shagiev.carwash.dto.user.operator.OperatorInfoDto;
import shagiev.carwash.dto.user.operator.OperatorRequestDto;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.user.AppUser;
import shagiev.carwash.model.user.AppUserRole;
import shagiev.carwash.model.user.OperatorInfo;
import shagiev.carwash.repo.AppUserRepo;
import shagiev.carwash.repo.CarBoxRepo;
import shagiev.carwash.repo.OperatorInfoRepo;
import shagiev.carwash.service.exceptions.NoSuchIdException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepo appUserRepo;
    private final CarBoxRepo carBoxRepo;
    private final OperatorInfoRepo operatorInfoRepo;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto register(AppUserRequestDto requestDto) {
        AppUser user = conversionService.convert(requestDto, AppUser.class);
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("user already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = appUserRepo.save(user);
        return conversionService.convert(savedUser, AppUserDto.class);
    }

    @Override
    @Transactional
    public OperatorInfoDto makeUserOperator(OperatorRequestDto requestDto) {
        AppUser user = appUserRepo.findById(requestDto.getUserId())
                .orElseThrow(() -> new NoSuchIdException("no such user"));
        if (user == null) {
            throw new NoSuchIdException("no such user");
        }
        CarBox carBox = carBoxRepo.findById(requestDto.getCarboxId())
                .orElseThrow(() -> new NoSuchIdException("no such carBox"));
        if (carBox == null) {
            throw new NoSuchIdException("no such carBox");
        }
        updateUserRole(user, AppUserRole.OPERATOR);
        OperatorInfo operatorInfo = operatorInfoRepo.save(
                new OperatorInfo(0, user, carBox, requestDto.getMinSale(), requestDto.getMaxSale()));
        return conversionService.convert(operatorInfo, OperatorInfoDto.class);
    }

    private void updateUserRole(AppUser user, AppUserRole role) {
        appUserRepo.save(new AppUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                role
        ));
    }

    @Override
    public OperatorInfoDto updateOperator(OperatorRequestDto requestDto) {
        OperatorInfo operatorInfo = operatorInfoRepo.findByUser_Id(requestDto.getUserId());
        if (operatorInfo == null) {
            throw new NoSuchIdException("no such operator id");
        }
        CarBox carBox = carBoxRepo.findById(requestDto.getCarboxId())
                .orElseThrow(() -> new NoSuchIdException("no such carBox"));
        if (carBox == null) {
            throw new NoSuchIdException("no such carBox");
        }
        OperatorInfo resultOperatorInfo = operatorInfoRepo.save(
                new OperatorInfo(operatorInfo.getId(),
                        operatorInfo.getUser(),
                        carBox,
                        requestDto.getMinSale(),
                        requestDto.getMaxSale())
        );
        return conversionService.convert(resultOperatorInfo, OperatorInfoDto.class);
    }

}
