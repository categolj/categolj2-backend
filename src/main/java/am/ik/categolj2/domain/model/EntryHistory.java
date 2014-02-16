package am.ik.categolj2.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "entry")
@Entity
@Table(name = "ENTRY_HISTORY", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE")
})
public class EntryHistory extends AbstractAuditableEntiry<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "ENTRY_HISTRY_ID")
    private String entryHistoryId;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "TITLE")
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    @Column(name = "CONTENTS")
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FORMAT")
    private String format;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRY_ID")
    @JsonIgnore
    private Entry entry;

    @Override
    public String getId() {
        return entryHistoryId;
    }

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }

}
