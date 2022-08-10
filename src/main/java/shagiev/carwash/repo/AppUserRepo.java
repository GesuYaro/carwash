package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.user.AppUser;

import java.util.List;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    @Transactional(readOnly = true)
    AppUser findByUsername(String username);

    @Transactional(readOnly = true)
    boolean existsByUsername(String username);

}
