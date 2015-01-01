package am.ik.categolj2.domain.repository.tag;

import am.ik.categolj2.domain.model.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TagAndEntryId {
    private final Tag tag;
    private final Integer entryId;
}
