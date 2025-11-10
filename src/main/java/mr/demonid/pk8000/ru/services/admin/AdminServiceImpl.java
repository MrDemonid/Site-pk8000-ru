package mr.demonid.pk8000.ru.services.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftCreateRequest;
import mr.demonid.pk8000.ru.dto.SoftUpdateRequest;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.CategoryRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.DescriptionCacheRefreshService;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.PathTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Сервисы по добавлению/изменению данных сайта.
 */
@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl {

    private SoftRepository softRepository;
    private CategoryRepository categoryRepository;
    private DescriptionCacheRefreshService descriptionCacheRefreshService;
    private ArchiveService archiveService;
    private ImageService imageService;

    private SoftMapper softMapper;
    private PathTool pathTool;


    /**
     * Добавляет в БД новый продукт (программу).
     * На этом этапе не учитываются списки файлов и изображений,
     * их можно добавить потом.
     */
    @Transactional
    public void createProduct(SoftCreateRequest request) {
        if (request == null || request.category() == null) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
        }
        try {
            CategoryEntity category = categoryRepository.findById(request.category()).orElse(null);
            if (category == null) {
                throw new ServiceException(ErrorCodes.BAD_SOFT_CATEGORY, "Неверная категория добавляемой программы");
            }
            SoftEntity soft = softMapper.toEntity(request, category);
            softRepository.save(soft);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление информации о продукте.
     * Кроме изображений и архивов, для них отдельные API.
     */
    @Transactional
    public void updateProduct(SoftUpdateRequest request) {
        if (request == null || request.category() == null || request.id() == null) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
        }
        try {
            CategoryEntity category = categoryRepository.findById(request.category()).orElse(null);
            if (category == null) {
                throw new ServiceException(ErrorCodes.BAD_SOFT_CATEGORY, "Неверная категория добавляемой программы");
            }
            SoftEntity soft = softRepository.findById(request.id()).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            soft.setName(request.name());
            soft.setCategory(category);
            soft.setShortDescription(request.shortDescription());

            softRepository.save(soft);

            log.info("Update product [ id = {}, name = '{}', short desc = '{}', category = '{}']", request.id(), request.name(), request.shortDescription(), category.getName());

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Удаление продукта.
     */
    @Transactional
    public void deleteProduct(Long productId) {
        try {
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт с id={} не найден" + productId);
            }
            log.info("Delete product [ id = {}]", soft);
            deleteImages(soft);
            deleteAttaches(soft);
            descriptionCacheRefreshService.deleteCache(productId);
            softRepository.delete(soft);

        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    private void deleteAttaches(SoftEntity soft) {
        archiveService.removePhysicFiles(soft.getId());
    }

    private void deleteImages(SoftEntity soft) {
        imageService.removePhysicFiles(soft.getId());
    }
}
