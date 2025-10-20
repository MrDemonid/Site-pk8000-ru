package mr.demonid.pk8000.ru.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl {


    public void addNewDocument() {
        log.info("Adding new document");
//        DocumentEntity documentEntity = documentMapper.mapToDocument(documentRequest);
//        try {
//            documentRepository.save(documentEntity);
//        } catch (Exception e) {
//            throw new PageException(ErrorCodes.DOCUMENT_SAVE_ERROR, e.getMessage());
//        }
    }


}
