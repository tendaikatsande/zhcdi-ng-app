package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FrameworkType;
import com.mycompany.myapp.repository.FrameworkTypeRepository;
import com.mycompany.myapp.service.FrameworkTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FrameworkType}.
 */
@RestController
@RequestMapping("/api")
public class FrameworkTypeResource {

    private final Logger log = LoggerFactory.getLogger(FrameworkTypeResource.class);

    private static final String ENTITY_NAME = "frameworkType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrameworkTypeService frameworkTypeService;

    private final FrameworkTypeRepository frameworkTypeRepository;

    public FrameworkTypeResource(FrameworkTypeService frameworkTypeService, FrameworkTypeRepository frameworkTypeRepository) {
        this.frameworkTypeService = frameworkTypeService;
        this.frameworkTypeRepository = frameworkTypeRepository;
    }

    /**
     * {@code POST  /framework-types} : Create a new frameworkType.
     *
     * @param frameworkType the frameworkType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frameworkType, or with status {@code 400 (Bad Request)} if the frameworkType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/framework-types")
    public ResponseEntity<FrameworkType> createFrameworkType(@Valid @RequestBody FrameworkType frameworkType) throws URISyntaxException {
        log.debug("REST request to save FrameworkType : {}", frameworkType);
        if (frameworkType.getId() != null) {
            throw new BadRequestAlertException("A new frameworkType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrameworkType result = frameworkTypeService.save(frameworkType);
        return ResponseEntity
            .created(new URI("/api/framework-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /framework-types/:id} : Updates an existing frameworkType.
     *
     * @param id the id of the frameworkType to save.
     * @param frameworkType the frameworkType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frameworkType,
     * or with status {@code 400 (Bad Request)} if the frameworkType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frameworkType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/framework-types/{id}")
    public ResponseEntity<FrameworkType> updateFrameworkType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrameworkType frameworkType
    ) throws URISyntaxException {
        log.debug("REST request to update FrameworkType : {}, {}", id, frameworkType);
        if (frameworkType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frameworkType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frameworkTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrameworkType result = frameworkTypeService.update(frameworkType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frameworkType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /framework-types/:id} : Partial updates given fields of an existing frameworkType, field will ignore if it is null
     *
     * @param id the id of the frameworkType to save.
     * @param frameworkType the frameworkType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frameworkType,
     * or with status {@code 400 (Bad Request)} if the frameworkType is not valid,
     * or with status {@code 404 (Not Found)} if the frameworkType is not found,
     * or with status {@code 500 (Internal Server Error)} if the frameworkType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/framework-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FrameworkType> partialUpdateFrameworkType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrameworkType frameworkType
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrameworkType partially : {}, {}", id, frameworkType);
        if (frameworkType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frameworkType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frameworkTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrameworkType> result = frameworkTypeService.partialUpdate(frameworkType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frameworkType.getId().toString())
        );
    }

    /**
     * {@code GET  /framework-types} : get all the frameworkTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frameworkTypes in body.
     */
    @GetMapping("/framework-types")
    public ResponseEntity<List<FrameworkType>> getAllFrameworkTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FrameworkTypes");
        Page<FrameworkType> page = frameworkTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /framework-types/:id} : get the "id" frameworkType.
     *
     * @param id the id of the frameworkType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frameworkType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/framework-types/{id}")
    public ResponseEntity<FrameworkType> getFrameworkType(@PathVariable Long id) {
        log.debug("REST request to get FrameworkType : {}", id);
        Optional<FrameworkType> frameworkType = frameworkTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frameworkType);
    }

    /**
     * {@code DELETE  /framework-types/:id} : delete the "id" frameworkType.
     *
     * @param id the id of the frameworkType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/framework-types/{id}")
    public ResponseEntity<Void> deleteFrameworkType(@PathVariable Long id) {
        log.debug("REST request to delete FrameworkType : {}", id);
        frameworkTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
