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
package am.ik.categolj2.domain.service.book;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.jaxws.Image;
import am.ik.aws.apa.jaxws.ItemAttributes;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import am.ik.categolj2.core.message.MessageKeys;
import org.springframework.stereotype.Service;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.inject.Inject;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Inject
    AwsApaRequester requester;

    List<BookDto> reseponseToBooks(ItemSearchResponse response) {
        return response.getItems().stream()
                .flatMap(items -> items.getItem().stream())
                .map(item -> {
                    BookDto book = new BookDto();
                    ItemAttributes attributes = item.getItemAttributes();
                    Image image = item.getMediumImage();
                    book.setAsin(item.getASIN());
                    book.setBookLink(item.getDetailPageURL());
                    if (image != null) {
                        book.setBookImage(image.getURL());
                    }
                    if (attributes != null) {
                        book.setBookTitle(attributes.getTitle());
                        book.setAuthors(attributes.getAuthor());
                        book.setPublicationDate(attributes.getPublicationDate());
                    }
                    return book;
                })
                .collect(Collectors.toList());
    }

    Response<ItemSearchResponse> searchBook(ItemSearchRequest request) throws ExecutionException, InterruptedException {
        request.getResponseGroup().add("Large");
        return requester.itemSearchAsync(request);
    }

    @Override
    public List<BookDto> searchByTitle(String title) {
        List<BookDto> books = new ArrayList<>();

        try {
            ItemSearchRequest requestForBooks = new ItemSearchRequest();
            requestForBooks.setTitle(title);
            requestForBooks.setSearchIndex("Books");
            Response<ItemSearchResponse> responseForBooks = searchBook(requestForBooks);

            ItemSearchRequest requestForForeignBooks = new ItemSearchRequest();
            requestForForeignBooks.setTitle(title);
            requestForForeignBooks.setSearchIndex("ForeignBooks");
            Response<ItemSearchResponse> responseForForeignBooks = searchBook(requestForForeignBooks);

            books.addAll(reseponseToBooks(responseForBooks.get()));
            books.addAll(reseponseToBooks(responseForForeignBooks.get()));
        } catch (ExecutionException | InterruptedException | WebServiceException  e) {
            throw new SystemException(MessageKeys.E_CT_BO_9501, e);
        }
        return books;
    }

    @Override
    public List<BookDto> searchByKeyword(String keyword) {
        List<BookDto> books = new ArrayList<>();

        try {
            ItemSearchRequest requestForBooks = new ItemSearchRequest();
            requestForBooks.setKeywords(keyword);
            requestForBooks.setSearchIndex("Books");
            Response<ItemSearchResponse> responseForBooks = searchBook(requestForBooks);

            ItemSearchRequest requestForForeignBooks = new ItemSearchRequest();
            requestForForeignBooks.setKeywords(keyword);
            requestForForeignBooks.setSearchIndex("ForeignBooks");
            Response<ItemSearchResponse> responseForForeignBooks = searchBook(requestForForeignBooks);

            books.addAll(reseponseToBooks(responseForBooks.get()));
            books.addAll(reseponseToBooks(responseForForeignBooks.get()));
        } catch (ExecutionException | InterruptedException | WebServiceException e) {
            throw new SystemException(MessageKeys.E_CT_BO_9501, e);
        }
        return books;
    }
}
