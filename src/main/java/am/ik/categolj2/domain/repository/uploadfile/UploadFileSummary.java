package am.ik.categolj2.domain.repository.uploadfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileSummary {
    private String fileId;
    private String fileName;
}
