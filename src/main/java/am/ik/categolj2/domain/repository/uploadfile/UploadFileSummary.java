package am.ik.categolj2.domain.repository.uploadfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Data
@AllArgsConstructor
public class UploadFileSummary {
    private String fileId;
    private String fileName;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastModifiedDate;
}
