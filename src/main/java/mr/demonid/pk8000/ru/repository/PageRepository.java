package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {

    Optional<PageEntity> findBySlug(String slug);
}
