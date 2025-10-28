package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SoftDescriptionFileRepository extends JpaRepository<SoftDescriptionFileEntity, Long> {

}
