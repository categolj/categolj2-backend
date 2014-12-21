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
package am.ik.categolj2.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import am.ik.categolj2.infra.search.DateTimeBridge;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

@MappedSuperclass
public abstract class AbstractAuditableEntiry<T extends Serializable> extends
        AbstractEntity implements Auditable<String, T> {
    private static final long serialVersionUID = 1L;
    @Column(name = "CREATED_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDate;
    @Column(name = "LAST_MODIFIED_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @FieldBridge(impl = DateTimeBridge.class)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private DateTime lastModifiedDate;
    @Column(name = "CREATED_BY")
    @Size(min = 1, max = 128)
    private String createdBy;
    @Column(name = "LAST_MODIFIED_BY")
    @Size(min = 1, max = 128)
    private String lastModifiedBy;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
