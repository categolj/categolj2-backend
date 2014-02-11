package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UploadFileService {
    UploadFile findOne(String fileId);

    UploadFileSummary findOneSummary(String fileId);

    Page<UploadFileSummary> findPage(Pageable pageable);

    UploadFile create(UploadFile uploadFile);

    void delete(String fileId);
}
