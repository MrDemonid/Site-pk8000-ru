package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.SoftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SoftRepository extends JpaRepository<SoftEntity, Long>, JpaSpecificationExecutor<SoftEntity> {

}
