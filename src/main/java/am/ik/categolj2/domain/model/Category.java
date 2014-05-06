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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "entry")
@Entity
@Table(name = "CATEGORY", indexes = {
        @Index(columnList = "CATEGORY_NAME")
})
public class Category implements Comparable<Category> {
    @EmbeddedId
    @JsonIgnore
    private CategoryPK categoryPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRY_ID", insertable = false, updatable = false)
    @JsonIgnore
    private Entry entry;

    public Category(Integer entryId, Integer categoryOrder, String categoryName) {
        this(new CategoryPK(entryId, categoryOrder), categoryName, null);
    }

    @Override
    public int compareTo(Category o) {
        return this.categoryPK.compareTo(o.categoryPK);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        if (categoryName == null) {
            if (other.categoryName != null)
                return false;
        } else if (!categoryName.equals(other.categoryName))
            return false;
        if (categoryPK == null) {
            if (other.categoryPK != null)
                return false;
        } else if (!categoryPK.equals(other.categoryPK))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((categoryName == null) ? 0 : categoryName.hashCode());
        result = prime * result
                + ((categoryPK == null) ? 0 : categoryPK.hashCode());
        return result;
    }

}
