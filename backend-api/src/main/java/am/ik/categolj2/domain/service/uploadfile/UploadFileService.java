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

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UploadFileService {
    UploadFile findOne(String fileId);

    UploadFileSummary findOneSummary(String fileId);

    Page<UploadFileSummary> findPage(Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    UploadFile create(UploadFile uploadFile);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<UploadFile> create(List<UploadFile> uploadFile);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void delete(String fileId);
}
