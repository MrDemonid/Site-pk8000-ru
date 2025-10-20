package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
