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
package am.ik.categolj2.api.category;

import am.ik.categolj2.App;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.service.accesslog.AccessLogHelper;
import am.ik.categolj2.domain.service.category.CategoryDto;
import am.ik.categolj2.domain.service.category.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("api/" + App.API_VERSION + "/categories")
public class CategoryRestController {
    @Inject
    CategoryService categoryService;
    @Inject
    AccessLogHelper accessLogHelper;

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDto> getCategories(AccessLog accessLog) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        return categoryService.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, params = "keyword", headers = Categolj2Headers.X_ADMIN)
    public List<CategoryDto> searchCategories(@RequestParam("keyword") String keyword) {
        return categoryService.search(keyword);
    }
}
