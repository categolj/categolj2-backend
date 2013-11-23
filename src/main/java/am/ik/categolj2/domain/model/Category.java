package am.ik.categolj2.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "entry")
@Entity
@Table(name = "CATEGORY")
public class Category implements Comparable<Category> {
	@EmbeddedId
	private CategoryPK categoryPK;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "CATEGORY_NAME")
	private String categoryName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTRY_ID", insertable = false, updatable = false)
	private Entry entry;

	public Category(Integer entryId, Integer categoryOrder, String categoryName) {
		this(new CategoryPK(entryId, categoryOrder), categoryName, null);
	}

	@Override
	public int compareTo(Category o) {
		return this.categoryPK.compareTo(o.categoryPK);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (categoryPK == null) {
			if (other.categoryPK != null)
				return false;
		} else if (!categoryPK.equals(other.categoryPK))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result
				+ ((categoryPK == null) ? 0 : categoryPK.hashCode());
		return result;
	}
}
