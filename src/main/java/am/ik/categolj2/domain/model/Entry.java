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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "histories" })
@Entity
@Indexed
@Table(name = "ENTRY")
public class Entry extends AbstractAuditableEntiry<Integer> {

	private static final long serialVersionUID = 1L;
	@GeneratedValue
	@Id
	@Column(name = "ENTRY_ID")
	private Integer entryId;
	@NotNull
	@Size(min = 1, max = 512)
	@Column(name = "TITLE")
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String title;
	@NotNull
	@Size(min = 1, max = 65536)
	@Column(name = "CONTENTS")
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String contents;
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "FORMAT")
	private String format;
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "entry")
	@OrderBy("CATEGORY_ORDER ASC")
	private List<Category> category;
	@Column(name = "PUBLISHED")
	@Basic(optional = false)
	private boolean published;
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "entry")
	@OrderBy("CREATED_DATE")
	@JsonIgnore
	private List<EntryHistory> histories;

	public Entry(Integer entryId, String title) {
		this.entryId = entryId;
		this.title = title;
	}

	@Override
	@Transient
	@JsonIgnore
	public Integer getId() {
		return entryId;
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isNew() {
		return entryId == null;
	}

	// public String toString() {
	// return "Entry[" + entryId + "]";
	// }
}
