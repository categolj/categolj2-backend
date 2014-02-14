package am.ik.categolj2.api.file;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.api.MediaTypeResolver;
import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import am.ik.categolj2.domain.service.uploadfile.UploadFileService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("files")
public class FileRestController {
    @Inject
    UploadFileService uploadFileService;
    @Inject
    FileHelper fileHelper;
    @Inject
    Mapper beanMapper;
    @Inject
    MediaTypeResolver mediaTypeResolver;
    @Value("${filedownload.cache.seconds}")
    int cacheSeconds;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public Page<FileResource> getFiles(@PageableDefault Pageable pageable) {
        Page<UploadFileSummary> summaries = uploadFileService.findPage(pageable);
        List<FileResource> resources = summaries.getContent().stream()
                .map(file -> beanMapper.map(file, FileResource.class))
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, summaries.getTotalElements());
    }

    @RequestMapping(method = RequestMethod.POST/*, headers = Categolj2Headers.X_ADMIN*/)
    public ResponseEntity<List<FileResource>> uploadFiles(FilesForm filesForm) {
        List<UploadFile> uploadFiles = filesForm.getFiles().stream()
                .map(fileHelper::multipartFileToUploadFile)
                .collect(Collectors.toList());
        List<FileResource> created = uploadFileService.create(uploadFiles).stream()
                .map(resource -> beanMapper.map(resource, FileResource.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{fileId}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") String fileId,
                                               @PathVariable("fileName") String fileName, HttpServletRequest request) {
        UploadFileSummary summary = uploadFileService.findOneSummary(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeAttachmentIfNeccessary(summary.getFileName())
                .lastModified(summary.getLastModifiedDate())
                .cacheForSeconds(cacheSeconds, false)
                .build();
        long ifModifiedSince = request.getDateHeader(com.google.common.net.HttpHeaders.IF_MODIFIED_SINCE);
        fileHelper.checkFileName(summary.getFileName(), fileName);
        return fileHelper.creteHttpResponse(ifModifiedSince, summary, headers);
    }

    @RequestMapping(value = "{fileId}/{fileName:.+}", method = RequestMethod.GET, params = "no-cache")
    public ResponseEntity<byte[]> downloadFileNoCache(@PathVariable("fileId") String fileId,
                                                      @PathVariable("fileName") String fileName) {
        UploadFile uploadFile = uploadFileService.findOne(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeAttachmentIfNeccessary(uploadFile.getFileName())
                .lastModified(uploadFile.getLastModifiedDate())
                .preventCaching()
                .build();
        fileHelper.checkFileName(uploadFile.getFileName(), fileName);
        return new ResponseEntity<>(uploadFile.getFileContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{fileId}/{fileName:.+}", method = RequestMethod.GET, params = "attachment")
    public ResponseEntity<byte[]> downloadFileForAttachment(@PathVariable("fileId") String fileId,
                                                            @PathVariable("fileName") String fileName) {
        UploadFile uploadFile = uploadFileService.findOne(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeForceAttachment(uploadFile.getFileName())
                .lastModified(uploadFile.getLastModifiedDate())
                .build();
        fileHelper.checkFileName(uploadFile.getFileName(), fileName);
        return new ResponseEntity<>(uploadFile.getFileContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{fileId}/{fileName:.+}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") String fileId,
                                           @PathVariable("fileName") String fileName) {
        uploadFileService.delete(fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
