package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FileUpload;
import com.mycompany.myapp.repository.FileUploadRepository;
import com.mycompany.myapp.service.FileUploadService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FileUpload}.
 */
@RestController
@RequestMapping("/api")
public class FileUploadResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    private static final String ENTITY_NAME = "fileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileUploadService fileUploadService;

    private final FileUploadRepository fileUploadRepository;

    public FileUploadResource(FileUploadService fileUploadService, FileUploadRepository fileUploadRepository) {
        this.fileUploadService = fileUploadService;
        this.fileUploadRepository = fileUploadRepository;
    }

    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUpload the fileUpload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUpload, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-uploads")
    public ResponseEntity<FileUpload> createFileUpload(@Valid @RequestBody FileUpload fileUpload) throws URISyntaxException {
        log.debug("REST request to save FileUpload : {}", fileUpload);
        if (fileUpload.getId() != null) {
            throw new BadRequestAlertException("A new fileUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileUpload result = fileUploadService.save(fileUpload);
        return ResponseEntity
            .created(new URI("/api/file-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-uploads/:id} : Updates an existing fileUpload.
     *
     * @param id the id of the fileUpload to save.
     * @param fileUpload the fileUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUpload,
     * or with status {@code 400 (Bad Request)} if the fileUpload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-uploads/{id}")
    public ResponseEntity<FileUpload> updateFileUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileUpload fileUpload
    ) throws URISyntaxException {
        log.debug("REST request to update FileUpload : {}, {}", id, fileUpload);
        if (fileUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileUpload result = fileUploadService.update(fileUpload);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileUpload.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-uploads/:id} : Partial updates given fields of an existing fileUpload, field will ignore if it is null
     *
     * @param id the id of the fileUpload to save.
     * @param fileUpload the fileUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUpload,
     * or with status {@code 400 (Bad Request)} if the fileUpload is not valid,
     * or with status {@code 404 (Not Found)} if the fileUpload is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-uploads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileUpload> partialUpdateFileUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileUpload fileUpload
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileUpload partially : {}, {}", id, fileUpload);
        if (fileUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileUpload> result = fileUploadService.partialUpdate(fileUpload);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileUpload.getId().toString())
        );
    }

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    @GetMapping("/file-uploads")
    public ResponseEntity<List<FileUpload>> getAllFileUploads(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FileUploads");
        Page<FileUpload> page = fileUploadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUpload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUpload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-uploads/{id}")
    public ResponseEntity<FileUpload> getFileUpload(@PathVariable Long id) {
        log.debug("REST request to get FileUpload : {}", id);
        Optional<FileUpload> fileUpload = fileUploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileUpload);
    }

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUpload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-uploads/{id}")
    public ResponseEntity<Void> deleteFileUpload(@PathVariable Long id) {
        log.debug("REST request to delete FileUpload : {}", id);
        fileUploadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
