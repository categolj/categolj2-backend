package am.ik.categolj2.domain.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLE")
public class Role implements Auditable<String, Integer> {
	private static final long serialVersionUID = 4518188757127538670L;
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "ROLE_ID")
	private Integer roleId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 25)
	@Column(name = "ROLE_NAME", unique = true)
	private String roleName;
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;
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

	public Role(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	@Transient
	public Integer getId() {
		return roleId;
	}

	@Override
	@Transient
	public boolean isNew() {
		return roleId == null;
	}

	@Override
	public String toString() {
		return "Role[" + roleName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}
}
