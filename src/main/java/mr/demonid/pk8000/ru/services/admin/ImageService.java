package mr.demonid.pk8000.ru.services.admin;


import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.ImagesEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.ImageResponse;
import mr.demonid.pk8000.ru.repository.ProductImagesRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.FileType;
import mr.demonid.pk8000.ru.util.PathTool;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;


/**
 * Сервис управления изображениями динамических страниц.
 */
@Service
public class ImageService extends BaseFileService<ImagesEntity, ProductImagesRepository, ImageResponse> {

    public ImageService(SoftRepository softRepository,
                        ProductImagesRepository repository,
                        AppConfiguration config,
                        PathTool pathTool,
                        SoftMapper softMapper) {
        super(softRepository, repository, config, pathTool, softMapper);
    }


    @Override
    protected boolean isValidFileType(Path file) {
        return FileType.isImage(file);
    }

    @Override
    protected String getSubdirectory() {
        return pathTool.getSoftImagesSubdir();
    }

    @Override
    protected String getFileTypeName() {
        return "image";
    }

    @Override
    protected List<ImageResponse> mapResponse(SoftEntity soft) {
        return softMapper.toImageResponse(soft);
    }

    @Override
    protected ImagesEntity createEntity() {
        return new ImagesEntity();
    }
}