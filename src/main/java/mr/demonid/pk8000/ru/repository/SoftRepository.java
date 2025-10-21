package mr.demonid.pk8000.ru.repository;

import mr.demonid.pk8000.ru.domain.SoftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SoftRepository extends JpaRepository<SoftEntity, Long>, JpaSpecificationExecutor<SoftEntity> {

    /**
     * Выборка товара по его ID, с принудительной загрузкой категории.
     */
    @Query("SELECT p FROM SoftEntity p JOIN FETCH p.category WHERE p.id = :id")
    Optional<SoftEntity> findByIdWithCategory(@Param("id") Long id);

}
