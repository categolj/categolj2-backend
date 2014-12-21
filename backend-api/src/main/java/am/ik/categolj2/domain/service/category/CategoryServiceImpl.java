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
package am.ik.categolj2.domain.service.category;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        List<String> categoryStrings = categoryRepository.findAllConcatenatedCategory();
        return categoryStrings.stream()
                .map(string -> new CategoryDto(Categories.fromCategory(string)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> search(String keyword) {
        String categoryName = QueryEscapeUtils.toContainingCondition(keyword);
        List<String> categoryStrings = categoryRepository.findConcatenatedCategoryLikeCategoryName(categoryName);
        return categoryStrings.stream()
                .map(string -> new CategoryDto(Categories.fromCategory(string)))
                .collect(Collectors.toList());
    }
}
