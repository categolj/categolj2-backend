package am.ik.categolj2.api.file;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.api.MediaTypeResolver;
import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import am.ik.categolj2.domain.service.uploadfile.UploadFileService;
import com.google.common.net.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<FileResource> postFile(FileResource fileResource) {
        UploadFile uploadFile = fileHelper.multipartFileToUploadFile(fileResource.getFile());
        UploadFile created = uploadFileService.create(uploadFile);

        return new ResponseEntity<>(beanMapper.map(created, FileResource.class), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") String fileId, HttpServletRequest request) {
        UploadFileSummary summary = uploadFileService.findOneSummary(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeAttachmentIfNeccessary(summary.getFileName())
                .lastModified(summary.getLastModifiedDate())
                .cacheForSeconds(cacheSeconds, false)
                .build();
        long ifModifiedSince = request.getDateHeader(com.google.common.net.HttpHeaders.IF_MODIFIED_SINCE);

        return fileHelper.creteHttpResponse(ifModifiedSince, summary, headers);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.GET, params = "no-cache")
    public ResponseEntity<byte[]> downloadFileNoCache(@PathVariable("fileId") String fileId) {
        UploadFile uploadFile = uploadFileService.findOne(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeAttachmentIfNeccessary(uploadFile.getFileName())
                .lastModified(uploadFile.getLastModifiedDate())
                .preventCaching()
                .build();
        return new ResponseEntity<>(uploadFile.getFileContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.GET, params = "attachment")
    public ResponseEntity<byte[]> downloadFileForAttachment(@PathVariable("fileId") String fileId) {
        UploadFile uploadFile = uploadFileService.findOne(fileId);
        HttpHeaders headers = new HttpHeadersBuilder(mediaTypeResolver)
                .contentTypeForceAttachment(uploadFile.getFileName())
                .lastModified(uploadFile.getLastModifiedDate())
                .build();
        return new ResponseEntity<>(uploadFile.getFileContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{fileId}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") String fileId) {
        uploadFileService.delete(fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
