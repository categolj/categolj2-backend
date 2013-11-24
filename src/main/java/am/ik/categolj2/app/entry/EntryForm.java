package am.ik.categolj2.app.entry;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EntryForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer entryId;
	@NotNull
	@Size(min = 1, max = 512)
	private String title;
	@NotNull
	@Size(min = 1, max = 65536)
	private String contents;
	@NotNull
	@Size(min = 1, max = 10)
	private String format;
	@NotNull
	private String categoryString;
	private boolean published;

	private boolean updateLastModifiedDate;
	private boolean saveInHistory;

}
