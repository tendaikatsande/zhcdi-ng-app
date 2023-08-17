package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FileUpload;
import com.mycompany.myapp.repository.FileUploadRepository;
import com.mycompany.myapp.service.FileUploadService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileUpload}.
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadRepository fileUploadRepository;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    public FileUpload save(FileUpload fileUpload) {
        log.debug("Request to save FileUpload : {}", fileUpload);
        return fileUploadRepository.save(fileUpload);
    }

    @Override
    public FileUpload update(FileUpload fileUpload) {
        log.debug("Request to update FileUpload : {}", fileUpload);
        return fileUploadRepository.save(fileUpload);
    }

    @Override
    public Optional<FileUpload> partialUpdate(FileUpload fileUpload) {
        log.debug("Request to partially update FileUpload : {}", fileUpload);

        return fileUploadRepository
            .findById(fileUpload.getId())
            .map(existingFileUpload -> {
                if (fileUpload.getFilename() != null) {
                    existingFileUpload.setFilename(fileUpload.getFilename());
                }
                if (fileUpload.getOriginalFilename() != null) {
                    existingFileUpload.setOriginalFilename(fileUpload.getOriginalFilename());
                }
                if (fileUpload.getExtension() != null) {
                    existingFileUpload.setExtension(fileUpload.getExtension());
                }
                if (fileUpload.getSizeInBytes() != null) {
                    existingFileUpload.setSizeInBytes(fileUpload.getSizeInBytes());
                }
                if (fileUpload.getSha256() != null) {
                    existingFileUpload.setSha256(fileUpload.getSha256());
                }
                if (fileUpload.getContentType() != null) {
                    existingFileUpload.setContentType(fileUpload.getContentType());
                }
                if (fileUpload.getUploadDate() != null) {
                    existingFileUpload.setUploadDate(fileUpload.getUploadDate());
                }

                return existingFileUpload;
            })
            .map(fileUploadRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileUpload> findAll(Pageable pageable) {
        log.debug("Request to get all FileUploads");
        return fileUploadRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileUpload> findOne(Long id) {
        log.debug("Request to get FileUpload : {}", id);
        return fileUploadRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileUpload : {}", id);
        fileUploadRepository.deleteById(id);
    }
}
