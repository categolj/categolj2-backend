package am.ik.categolj2.domain.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CategoryPK implements Comparable<CategoryPK>, Serializable {
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ENTRY_ID")
	private Integer entryId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CATEGORY_ORDER")
	private Integer categoryOrder;

	@Override
	public int compareTo(CategoryPK o) {
		return this.categoryOrder.compareTo(o.categoryOrder);
	}
}
