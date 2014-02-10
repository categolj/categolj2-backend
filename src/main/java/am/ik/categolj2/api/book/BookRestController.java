package am.ik.categolj2.api.book;

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
@RequestMapping("books")
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
