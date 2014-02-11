package am.ik.categolj2.domain.repository.uploadfile;


import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UploadFileRepository extends JpaRepository<UploadFile, String> {
    @Query(value = "SELECT NEW am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary(x.fileId, x.fileName) FROM UploadFile x ORDER BY x.lastModifiedDate DESC",
            countQuery = "SELECT COUNT(x) FROM UploadFile x")
    Page<UploadFileSummary> findSummaryPageOrderByLastModifiedDateDesc(Pageable pageable);
}
