package am.ik.categolj2.api.tag;

import am.ik.categolj2.domain.validation.TagName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagResource implements Comparable<TagResource> {
    @NotNull
    @TagName
    private String tagName;

    @Override
    public int compareTo(TagResource o) {
        return Objects.compare(this, o, Comparator.comparing(TagResource::getTagName));
    }
}
