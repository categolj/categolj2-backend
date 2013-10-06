package am.ik.categolj2.domain.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class User implements Auditable<String, String> {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "USERNAME")
	private String username;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 256)
	@Column(name = "PASSWORD")
	private String password;
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "the given email is invalid.")
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "EMAIL")
	private String email;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ENABLED")
	private boolean enabled;
	@Basic(optional = false)
	@NotNull
	@Column(name = "LOCKED")
	private boolean locked;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "LAST_NAME")
	private String lastName;

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
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USERNAME") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Role> roles;

	public User(String username) {
		this.username = username;
	}

	@Transient
	@Override
	public String getId() {
		return username;
	}

	@Transient
	@Override
	public boolean isNew() {
		return username == null;
	}

	@Override
	public String toString() {
		return "User[" + username + "]";
	}

	// business methods
	@Transient
	public User lock() {
		this.locked = true;
		return this;
	}

	@Transient
	public User unlock() {
		this.locked = false;
		return this;
	}

	@Transient
	public User enable() {
		this.enabled = true;
		return this;
	}

	@Transient
	public User disable() {
		this.enabled = false;
		return this;
	}
}
