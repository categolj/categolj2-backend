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

import java.util.*;
import java.util.stream.Collectors;

import lombok.Data;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

@Data
public class Categories {
    public static final String SEPARATOR = "::";

    private final List<Category> categories;

    private static final Function<Category, String> CATEGORY_TO_STRING = new Function<Category, String>() {
        @Override
        public String apply(Category category) {
            return category.getCategoryName();
        }
    };

    public static String toString(Categories categories) {
        return toString(categories.getCategories());
    }

    public static String toString(List<Category> categories) {
        return categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.joining(Categories.SEPARATOR));
    }

    public static Categories fromCategory(String categoryString) {
        Iterable<String> names = Splitter.on(Categories.SEPARATOR)
                .omitEmptyStrings().split(categoryString);
        int i = 1;
        List<Category> categories = new ArrayList<>();
        for (String name : names) {
            categories.add(new Category(null, i++, name));
        }
        return new Categories(categories);
    }

    public Categories applyEntryId(Integer entryId) {
        categories.forEach(c -> c.getCategoryPK().setEntryId(entryId));
        return this;
    }
}
