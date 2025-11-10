package mr.demonid.pk8000.ru.services.admin;

import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.ArchivesEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.ArchiveResponse;
import mr.demonid.pk8000.ru.repository.ProductArchivesRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.FileType;
import mr.demonid.pk8000.ru.util.PathTool;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;


/**
 * Сервис управления файлами динамических страниц.
 */
@Service
public class ArchiveService extends BaseFileService<ArchivesEntity, ProductArchivesRepository, ArchiveResponse> {

    public ArchiveService(SoftRepository softRepository,
                          ProductArchivesRepository repository,
                          AppConfiguration config,
                          PathTool pathTool,
                          SoftMapper softMapper) {
        super(softRepository, repository, config, pathTool, softMapper);
    }


    @Override
    protected boolean isValidFileType(Path file) {
        return FileType.isArchive(file);
    }

    @Override
    protected String getSubdirectory() {
        return pathTool.getSoftFilesSubdir();
    }

    @Override
    protected String getFileTypeName() {
        return "archive";
    }

    @Override
    protected List<ArchiveResponse> mapResponse(SoftEntity soft) {
        return softMapper.toArchiveResponse(soft);
    }

    @Override
    protected ArchivesEntity createEntity() {
        return new ArchivesEntity();
    }
}