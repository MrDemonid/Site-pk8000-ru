package mr.demonid.pk8000.ru.repository;

import java.util.List;
import java.util.Optional;


public interface ProductFileRepository<T> {
    Optional<List<T>> findByProductId(Long productId);
}