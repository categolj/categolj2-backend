package am.ik.categolj2.domain.repository.uploadfile;


import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UploadFileRepository extends JpaRepository<UploadFile, String> {
    static final String SELECT_SUMMARY = "SELECT NEW am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary(x.fileId, x.fileName, x.lastModifiedDate)";

    @Query(value = SELECT_SUMMARY + " FROM UploadFile x ORDER BY x.lastModifiedDate DESC",
            countQuery = "SELECT COUNT(x) FROM UploadFile x")
    Page<UploadFileSummary> findSummaryPageOrderByLastModifiedDateDesc(Pageable pageable);


    @Query(SELECT_SUMMARY + " FROM UploadFile x WHERE x.fileId = :fileId")
    UploadFileSummary findOneSummary(@Param("fileId") String fileId);
}
