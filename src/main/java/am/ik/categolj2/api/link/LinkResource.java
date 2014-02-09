package am.ik.categolj2.api.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkResource {
    private String id;
    @NotNull
    @Size(min = 1, max = 128)
    private String url;
    @NotNull
    @Size(min = 1, max = 128)
    private String linkName;
}
