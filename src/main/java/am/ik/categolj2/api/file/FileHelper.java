package am.ik.categolj2.api.file;

import am.ik.categolj2.api.MediaTypeResolver;
import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class FileHelper {

    @Inject
    MediaTypeResolver mediaTypeResolver;


    UploadFile createFile(MultipartFile multipartFile) {
        UploadFile uploadFile = new UploadFile();
        try {
            uploadFile.setFileContent(multipartFile.getBytes());
        } catch (IOException e) {
            throw new SystemException("upload failed", e);
        }
        return uploadFile;
    }

    ResponseEntity<byte[]> createResponseEntity(UploadFile uploadFile) {
        MediaType mediaType = mediaTypeResolver.resolveFromFilename(uploadFile.getFileName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", uploadFile.getFileName());
        return new ResponseEntity<>(uploadFile.getFileContent(), headers, HttpStatus.OK);
    }
}
