package am.ik.categolj2.domain.service.book;

import java.util.List;

public interface BookService {
    List<BookDto> searchByTitle(String title);

    List<BookDto> searchByKeyword(String keyword);

}
