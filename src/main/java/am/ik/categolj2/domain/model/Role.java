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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users", callSuper = false)
@Entity
@Table(name = "ROLE")
public class Role extends AbstractAuditableEntiry<Integer> {
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
    @JsonIgnore
	private Set<User> users;

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
}
