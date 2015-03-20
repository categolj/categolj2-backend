package am.ik.categolj2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "CONFIG")
public class Config extends AbstractAuditableEntiry<String> {
    @Id
    @Column(name = "CONFIG_NAME")
    @Size(min = 0, max = 512)
    private String configName;

    @Column(name = "CONFIG_VALUE")
    @NotNull
    @Size(min = 0, max = 2048)
    private String configValue;

    @Override
    public String getId() {
        return configName;
    }

    @Override
    public boolean isNew() {
        return configName == null;
    }
}
