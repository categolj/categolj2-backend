package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UploadFileService {
    UploadFile findOne(String fileId);

    Page<UploadFile> findAllPage(Pageable pageable);

    UploadFile create(UploadFile uploadFile);

    void delete(String fileId);
}
