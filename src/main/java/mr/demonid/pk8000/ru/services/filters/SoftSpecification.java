package mr.demonid.pk8000.ru.services.filters;


import jakarta.persistence.criteria.Predicate;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Создание фильтра для выборки из БД.
 */
public class SoftSpecification {

    public static Specification<SoftEntity> filter(SoftFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (filter.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            if (filter.getSearchText() != null && !filter.getSearchText().isBlank()) {
                String pattern = "%" + filter.getSearchText().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
                Predicate shortDescPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("shortDescription")), pattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);

                predicates.add(criteriaBuilder.or(namePredicate, shortDescPredicate, descPredicate));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
