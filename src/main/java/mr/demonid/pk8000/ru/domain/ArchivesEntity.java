package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * Список файлов у продукта.
 */
@Entity
@Table(name = "product_archives")
public class ArchivesEntity extends ProductFileBase {
}