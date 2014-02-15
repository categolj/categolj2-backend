package am.ik.categolj2.domain.service.book;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface BookService {
    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<BookDto> searchByTitle(String title);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<BookDto> searchByKeyword(String keyword);

}
