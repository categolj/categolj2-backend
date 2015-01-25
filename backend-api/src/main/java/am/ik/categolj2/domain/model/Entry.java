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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"histories", "contents", "category"})
@Entity
@Indexed
@Table(name = "ENTRY", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE"),
        @javax.persistence.Index(columnList = "CREATED_BY")
})
public class Entry extends AbstractAuditableEntiry<Integer> {

    private static final long serialVersionUID = 1L;
    @GeneratedValue
    @Id
    @Column(name = "ENTRY_ID")
    private Integer entryId;
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "TITLE")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    @Column(name = "CONTENTS")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FORMAT")
    private String format;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "entry")
    @OrderBy("CATEGORY_ORDER ASC")
    private List<Category> category;
    @Column(name = "PUBLISHED")
    @Basic(optional = false)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private boolean published;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "entry")
    @OrderBy("CREATED_DATE")
    @JsonIgnore
    private List<EntryHistory> histories;
    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.LAZY
            , cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @OrderBy("TAG_NAME")
    @JoinTable(name = "ENTRY_TAGS",
            joinColumns = {@JoinColumn(name = "ENTRY", referencedColumnName = "ENTRY_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TAGS", referencedColumnName = "TAG_NAME")})
    @IndexedEmbedded
    @Valid
    private Set<Tag> tags;

    public Entry(Integer entryId, String title) {
        this.entryId = entryId;
        this.title = title;
    }

    @Override
    @Transient
    public Integer getId() {
        return entryId;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return entryId == null;
    }

    // public String toString() {
    // return "Entry[" + entryId + "]";
    // }
}
