package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * Список изображений у продукта.
 */
@Entity
@Table(name = "product_images")
public class ImagesEntity extends ProductFileBase {
}
