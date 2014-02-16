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
package am.ik.categolj2.domain.repository.uploadfile;


import am.ik.categolj2.domain.model.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UploadFileRepository extends JpaRepository<UploadFile, String> {
    static final String SELECT_SUMMARY = "SELECT NEW am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary(x.fileId, x.fileName, x.lastModifiedDate)";

    @Query(value = SELECT_SUMMARY + " FROM UploadFile x ORDER BY x.lastModifiedDate DESC",
            countQuery = "SELECT COUNT(x) FROM UploadFile x")
    Page<UploadFileSummary> findSummaryPageOrderByLastModifiedDateDesc(Pageable pageable);


    @Query(SELECT_SUMMARY + " FROM UploadFile x WHERE x.fileId = :fileId")
    UploadFileSummary findOneSummary(@Param("fileId") String fileId);
}
