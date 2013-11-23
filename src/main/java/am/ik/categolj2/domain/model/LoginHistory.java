package am.ik.categolj2.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoginHistory {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Size(min = 36, max = 36)
	@Column(name = "LOGIN_HISTRY_ID")
	private String loginHistoryId;

	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "USERNAME", length = 128)
	private String username;

	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "LOGIN_HOST")
	private String loginHost;

	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "LOGIN_AGENT", length = 128)
	private String loginAgent;

	@NotNull
	@Column(name = "CREATED_DATE")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime loginDate;
}
