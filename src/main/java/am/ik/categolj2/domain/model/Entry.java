package am.ik.categolj2.domain.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Indexed
@Table(name = "ENTRY")
public class Entry implements Auditable<String, Integer> {
	private static final long serialVersionUID = 1L;
	@GeneratedValue
	@Id
	@Column(name = "ENTRY_ID")
	private Integer entryId;
	@NotNull
	@Size(min = 1, max = 512)
	@Column(name = "TITLE")
	private String title;
	@NotNull
	@Size(min = 1, max = 65536)
	@Column(name = "CONTENTS")
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String contents;
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "entry")
	@OrderBy("CATEGORY_ORDER ASC")
	private List<Category> category;
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
	@Column(name = "PUBLISHED")
	@Basic(optional = false)
	private boolean published;
	@Version
	@Column(name = "VERSION")
	private Long version;

	public Entry(Integer entryId, String title) {
		this.entryId = entryId;
		this.title = title;
	}

	@Override
	@Transient
	public Integer getId() {
		return entryId;
	}

	@Override
	@Transient
	public boolean isNew() {
		return entryId == null;
	}

	// public String toString() {
	// return "Entry[" + entryId + "]";
	// }
}
