package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FileUpload;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FileUpload}.
 */
public interface FileUploadService {
    /**
     * Save a fileUpload.
     *
     * @param fileUpload the entity to save.
     * @return the persisted entity.
     */
    FileUpload save(FileUpload fileUpload);

    /**
     * Updates a fileUpload.
     *
     * @param fileUpload the entity to update.
     * @return the persisted entity.
     */
    FileUpload update(FileUpload fileUpload);

    /**
     * Partially updates a fileUpload.
     *
     * @param fileUpload the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileUpload> partialUpdate(FileUpload fileUpload);

    /**
     * Get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUpload> findAll(Pageable pageable);

    /**
     * Get the "id" fileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileUpload> findOne(Long id);

    /**
     * Delete the "id" fileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
