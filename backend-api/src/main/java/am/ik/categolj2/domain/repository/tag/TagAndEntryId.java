package am.ik.categolj2.domain.repository.tag;

import am.ik.categolj2.domain.model.Tag;
import lombok.Data;

@Data
public class TagAndEntryId {
    private final Tag tag;
    private final Integer entryId;
}
