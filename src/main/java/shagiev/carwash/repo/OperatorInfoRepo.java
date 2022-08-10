package shagiev.carwash.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shagiev.carwash.model.user.OperatorInfo;

@Repository
public interface OperatorInfoRepo extends JpaRepository<OperatorInfo, Long> {

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"user", "carBox"})
    OperatorInfo findByUser_Id(long userId);

}
