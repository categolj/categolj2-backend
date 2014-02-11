package am.ik.categolj2.api.file;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import am.ik.categolj2.domain.service.uploadfile.UploadFileService;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class FileHelper {
    @Inject
    UploadFileService uploadFileService;

    UploadFile multipartFileToUploadFile(MultipartFile multipartFile) {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileName(multipartFile.getOriginalFilename());
        try {
            uploadFile.setFileContent(multipartFile.getBytes());
        } catch (IOException e) {
            throw new SystemException("upload failed", e);
        }
        return uploadFile;
    }

    ResponseEntity<byte[]> creteHttpResponse(long ifModifiedSince, UploadFileSummary summary, HttpHeaders responseHeaders) {
        if (summary.getLastModifiedDate().isAfter(ifModifiedSince)) {
            UploadFile uploadFile = uploadFileService.findOne(summary.getFileId());
            return new ResponseEntity<>(uploadFile.getFileContent(), responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED);
        }
    }


}
