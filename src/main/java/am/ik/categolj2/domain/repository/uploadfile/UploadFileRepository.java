package am.ik.categolj2.domain.repository.uploadfile;


import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, String> {

}
