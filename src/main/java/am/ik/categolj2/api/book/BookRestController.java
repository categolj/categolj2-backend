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
package am.ik.categolj2.api.book;

import am.ik.categolj2.App;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.service.book.BookDto;
import am.ik.categolj2.domain.service.book.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("api/" + App.API_VERSION + "/books")
public class BookRestController {
    @Inject
    BookService bookService;

    @RequestMapping(method = RequestMethod.GET, params = "title", headers = Categolj2Headers.X_ADMIN)
    public List<BookDto> searchBooksByTitle(@RequestParam("title") String title) {
        return bookService.searchByTitle(title);
    }

    @RequestMapping(method = RequestMethod.GET, params = "keyword", headers = Categolj2Headers.X_ADMIN)
    public List<BookDto> searchBooksByKeyword(@RequestParam("keyword") String keyword) {
        return bookService.searchByKeyword(keyword);
    }
}
