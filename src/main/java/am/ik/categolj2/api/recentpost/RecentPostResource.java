package am.ik.categolj2.api.recentpost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentPostResource {
    private Integer entryId;
    private String title;
}
