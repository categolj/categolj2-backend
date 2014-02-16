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
package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileRepository;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;
import java.util.List;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Inject
    UploadFileRepository uploadFileRepository;
    @Inject
    DateFactory dateFactory;

    @Override
    public UploadFile findOne(String fileId) {
        UploadFile uploadFile = uploadFileRepository.findOne(fileId);
        if (uploadFile == null) {
            throw new ResourceNotFoundException("file is not found. [fileId="
                    + fileId + "]");
        }
        return uploadFile;
    }

    @Override
    @Cacheable(value = "uploadFileSummary")
    public UploadFileSummary findOneSummary(String fileId) {
        UploadFileSummary summary = uploadFileRepository.findOneSummary(fileId);
        if (summary == null) {
            throw new ResourceNotFoundException("file is not found. [fileId="
                    + fileId + "]");
        }
        return summary;
    }

    @Override
    public Page<UploadFileSummary> findPage(Pageable pageable) {
        return uploadFileRepository.findSummaryPageOrderByLastModifiedDateDesc(pageable);
    }

    @Override
    @Transactional
    public UploadFile create(UploadFile uploadFile) {
        DateTime now = dateFactory.newDateTime();
        uploadFile.setCreatedDate(now);
        uploadFile.setLastModifiedDate(now);
        return uploadFileRepository.save(uploadFile);
    }

    @Override
    @Transactional
    public List<UploadFile> create(List<UploadFile> uploadFiles) {
        DateTime now = dateFactory.newDateTime();

        uploadFiles.stream().forEach(uploadFile -> {
            uploadFile.setCreatedDate(now);
            uploadFile.setLastModifiedDate(now);
        });

        return uploadFileRepository.save(uploadFiles);
    }

    @Override
    @Transactional
    @CacheEvict(value = "uploadFileSummary")
    public void delete(String fileId) {
        uploadFileRepository.delete(fileId);
    }
}
