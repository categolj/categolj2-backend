package am.ik.categolj2.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LINK")
public class Link implements Auditable<String, String> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "URL")
	@NotNull
	@Size(min = 1, max = 128)
	private String url;
	@Column(name = "LINK_NAME")
	@NotNull
	@Size(min = 1, max = 128)
	private String linkName;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdDate;
	@Column(name = "LAST_MODIFIED_DATE")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime lastModifiedDate;
	@Column(name = "CREATED_BY")
	@Size(min = 1, max = 128)
	private String createdBy;
	@Column(name = "LAST_MODIFIED_BY")
	@Size(min = 1, max = 128)
	private String lastModifiedBy;
	@Version
	@Column(name = "VERSION")
	private Long version;

	@Transient
	@Override
	public String getId() {
		return url;
	}

	@Transient
	@Override
	public boolean isNew() {
		return url == null;
	}
}
