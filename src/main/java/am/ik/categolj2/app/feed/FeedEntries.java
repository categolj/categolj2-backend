package am.ik.categolj2.app.feed;

import am.ik.categolj2.domain.model.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FeedEntries {
    private final List<Entry> entries;
}
