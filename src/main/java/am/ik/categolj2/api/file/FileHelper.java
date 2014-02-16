/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.api.file;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import am.ik.categolj2.domain.service.uploadfile.UploadFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

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

    void checkFileName(String fileName, String requestedFileName) {
        if (!Objects.equals(fileName, requestedFileName)) {
            throw new ResourceNotFoundException("File not found. (fileName=" + requestedFileName + ")");
        }
    }


}
