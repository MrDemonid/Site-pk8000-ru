package mr.demonid.pk8000.ru.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.repository.SoftDescriptionFileRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.filters.SoftSpecification;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class SoftService {

    private final SoftRepository softRepository;
    private final SoftDescriptionFileRepository softDescriptionFileRepository;
    private final SoftMapper softMapper;


    /**
     * Возвращает постраничный список товаров для админки, с учетом фильтрации.
     */
    @Transactional(readOnly = true)
    public Page<SoftResponse> getAllProducts(SoftFilter softFilter, Pageable pageable) {
        Page<SoftEntity> items = softRepository.findAll(SoftSpecification.filter(softFilter), pageable);
        return items.map(softMapper::toResponse);
    }


    public String getDescription(Long productId) {
        Optional<SoftDescriptionFileEntity> entity = softDescriptionFileRepository.findByProduct_Id(productId);
        return entity.map(softMapper::descriptionToHtml).orElse(null);
    }

}
