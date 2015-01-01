package am.ik.categolj2.domain.model;

import am.ik.categolj2.domain.validation.TagName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "TAG")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"entries"})
@EqualsAndHashCode(exclude = {"entries"}, callSuper = false)
public class Tag {
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
}
