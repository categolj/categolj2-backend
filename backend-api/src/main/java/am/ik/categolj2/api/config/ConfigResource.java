package am.ik.categolj2.api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigResource implements Serializable {
    @NotNull
    @Size(min = 0, max = 512)
    private String configName;
    @NotNull
    @Size(min = 0, max = 2048)
    private String configValue;
}
