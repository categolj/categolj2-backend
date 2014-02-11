package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileRepository;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileSummary;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Inject
    UploadFileRepository uploadFileRepository;
    @Inject
    DateFactory dateFactory;

    @Override
    public UploadFile findOne(String fileId) {
        UploadFile uploadFile = uploadFileRepository.findOne(fileId);
        if (uploadFile == null) {
            throw new ResourceNotFoundException("file is not found. [fileId="
                    + fileId + "]");
        }
        return uploadFile;
    }

    @Override
    public Page<UploadFileSummary> findPage(Pageable pageable) {
        return uploadFileRepository.findSummaryPageOrderByLastModifiedDateDesc(pageable);
    }

    @Override
    @Transactional
    public UploadFile create(UploadFile uploadFile) {
        DateTime now = dateFactory.newDateTime();
        uploadFile.setCreatedDate(now);
        uploadFile.setLastModifiedDate(now);
        return uploadFileRepository.save(uploadFile);
    }

    @Override
    @Transactional
    public void delete(String fileId) {
        uploadFileRepository.delete(fileId);
    }
}
