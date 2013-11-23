package am.ik.categolj2.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

@MappedSuperclass
public abstract class AbstractAuditableEntiry<T extends Serializable> extends
		AbstractEntity implements Auditable<String, T> {
	private static final long serialVersionUID = 1L;
	@Column(name = "CREATED_DATE")
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

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public DateTime getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(DateTime creationDate) {
		this.createdDate = creationDate;
	}

	@Override
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
