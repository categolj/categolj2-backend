package am.ik.categolj2.domain.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class AbstractEntity {
	@Version
	@Column(name = "VERSION")
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
