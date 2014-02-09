package am.ik.categolj2.api.entry;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.service.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer entryId;
    @NotNull
    @Size(min = 1, max = 512)
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    private String format;
    @NotNull
    private String categoryString;

    private Long version;

    // input only
    private boolean published;
    private boolean updateLastModifiedDate;
    private boolean saveInHistory;

    // output only
    private List<String> categoryName;
    private DateTime createdDate;
    private DateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    @JsonIgnore
    EntryResource setCategoryName(List<Category> category) {
        this.categoryName = category.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
        return this;
    }
}
