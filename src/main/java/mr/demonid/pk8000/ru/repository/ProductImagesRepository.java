package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.ImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductImagesRepository extends JpaRepository<ImagesEntity, Long> {
    Optional<List<ImagesEntity>> findByProductId(Long productId);
}
