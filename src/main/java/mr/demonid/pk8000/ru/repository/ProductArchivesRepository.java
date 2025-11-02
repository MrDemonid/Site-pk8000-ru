package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.ArchivesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductArchivesRepository extends JpaRepository<ArchivesEntity, Long> {
    Optional<List<ArchivesEntity>> findByProductId(Long productId);
}
