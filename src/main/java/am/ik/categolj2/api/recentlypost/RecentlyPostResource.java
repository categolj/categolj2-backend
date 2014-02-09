package am.ik.categolj2.api.recentlypost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyPostResource {
    private Integer entryId;
    private String title;
}
