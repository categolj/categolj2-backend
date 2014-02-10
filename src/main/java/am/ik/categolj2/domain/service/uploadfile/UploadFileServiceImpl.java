package am.ik.categolj2.domain.service.uploadfile;

import am.ik.categolj2.domain.model.UploadFile;
import am.ik.categolj2.domain.repository.uploadfile.UploadFileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Inject
    UploadFileRepository uploadFileRepository;

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
    public Page<UploadFile> findAllPage(Pageable pageable) {
        return uploadFileRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public UploadFile create(UploadFile uploadFile) {
        return uploadFileRepository.save(uploadFile);
    }

    @Override
    @Transactional
    public void delete(String fileId) {
        uploadFileRepository.delete(fileId);
    }
}
