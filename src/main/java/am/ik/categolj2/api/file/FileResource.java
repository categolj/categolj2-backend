package am.ik.categolj2.api.file;

import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResource {
    private String fileId;
    private String fileName;
    private DateTime lastModifiedDate;


    @JsonProperty
    public String getFileExtension() {
        return Files.getFileExtension(fileName).toLowerCase(Locale.ENGLISH);
    }
}
