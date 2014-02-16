package am.ik.categolj2.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;

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
@ToString(exclude = "fileContent")
@Entity
@Table(name = "UPLOAD_FILE", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE")
})
public class UploadFile extends AbstractAuditableEntiry<String> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FILE_CONTENT")
    private byte[] fileContent;

    @Override
    public String getId() {
        return fileId;
    }

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }
}
