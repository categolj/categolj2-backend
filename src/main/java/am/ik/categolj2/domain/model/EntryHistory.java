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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "entry")
@Entity
@Table(name = "ENTRY_HISTORY", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE")
})
public class EntryHistory extends AbstractAuditableEntiry<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "ENTRY_HISTRY_ID")
    private String entryHistoryId;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "TITLE")
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    @Column(name = "CONTENTS")
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FORMAT")
    private String format;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRY_ID")
    @JsonIgnore
    private Entry entry;

    @Override
    public String getId() {
        return entryHistoryId;
    }

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }

}
