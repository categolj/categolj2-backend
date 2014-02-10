package am.ik.categolj2.api.file;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.service.uploadfile.UploadFileService;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

@RestController
@RequestMapping("files")
public class FileRestController {
    @Inject
    UploadFileService uploadFileService;
    @Inject
    FileHelper fileHelper;
    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FileResource> postFile(MultipartFile file) {
        UploadFile uploadFile = fileHelper.createFile(file);
        UploadFile created = uploadFileService.create(uploadFile);

        return new ResponseEntity<>(beanMapper.map(created, FileResource.class), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") String fileId) {
        UploadFile uploadFile = uploadFileService.findOne(fileId);
        return fileHelper.createResponseEntity(uploadFile);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") String fileId) {
        uploadFileService.delete(fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
