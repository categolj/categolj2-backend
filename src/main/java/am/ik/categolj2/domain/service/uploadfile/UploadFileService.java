package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UploadFileService {
    UploadFile findOne(String fileId);

    UploadFileSummary findOneSummary(String fileId);

    Page<UploadFileSummary> findPage(Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    UploadFile create(UploadFile uploadFile);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<UploadFile> create(List<UploadFile> uploadFile);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void delete(String fileId);
}
