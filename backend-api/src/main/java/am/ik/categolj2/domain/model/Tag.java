package am.ik.categolj2.domain.model;

import am.ik.categolj2.domain.validation.TagName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TAG")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"entries"})
@EqualsAndHashCode(exclude = {"entries"}, callSuper = false)
public class Tag implements Comparable<Tag>, Serializable {
    @Id
    @NotNull
    @TagName
    @Column(name = "TAG_NAME")
    private String tagName;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Entry> entries;

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public int compareTo(Tag o) {
        return Objects.compare(this, o, Comparator.comparing(Tag::getTagName));
    }
}
