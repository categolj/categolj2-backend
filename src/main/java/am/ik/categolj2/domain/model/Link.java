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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LINK", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE")
})
public class Link extends AbstractAuditableEntiry<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "URL")
    @NotNull
    @Size(min = 1, max = 128)
    private String url;
    @Column(name = "LINK_NAME")
    @NotNull
    @Size(min = 1, max = 128)
    private String linkName;

    @Transient
    @Override
    public String getId() {
        return url;
    }

    @Transient
    @Override
    public boolean isNew() {
        return url == null;
    }
}
