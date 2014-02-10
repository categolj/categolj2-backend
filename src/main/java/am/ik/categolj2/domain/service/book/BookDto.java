package am.ik.categolj2.domain.service.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String asin;
    private String bookTitle;
    private String bookImage;
    private String bookLink;
    private String publicationDate;
    private List<String> authors;
}
