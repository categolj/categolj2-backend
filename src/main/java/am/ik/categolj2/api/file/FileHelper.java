package am.ik.categolj2.api.file;

import am.ik.categolj2.domain.model.UploadFile;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.SystemException;

import java.io.IOException;

@Component
public class FileHelper {

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

    ResponseEntity<byte[]> creteHttpResponse(long ifModifiedSince, UploadFile uploadFile, HttpHeaders responseHeaders) {
        DateTime lastModified = uploadFile.getLastModifiedDate();
        if (lastModified.isAfter(ifModifiedSince)) {
            return new ResponseEntity<>(uploadFile.getFileContent(), responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED);
        }
    }


}
