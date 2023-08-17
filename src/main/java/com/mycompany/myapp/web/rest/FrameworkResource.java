package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Framework;
import com.mycompany.myapp.repository.FrameworkRepository;
import com.mycompany.myapp.service.FrameworkService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Framework}.
 */
@RestController
@RequestMapping("/api")
public class FrameworkResource {

    private final Logger log = LoggerFactory.getLogger(FrameworkResource.class);

    private static final String ENTITY_NAME = "framework";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrameworkService frameworkService;

    private final FrameworkRepository frameworkRepository;

    public FrameworkResource(FrameworkService frameworkService, FrameworkRepository frameworkRepository) {
        this.frameworkService = frameworkService;
        this.frameworkRepository = frameworkRepository;
    }

    /**
     * {@code POST  /frameworks} : Create a new framework.
     *
     * @param framework the framework to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new framework, or with status {@code 400 (Bad Request)} if the framework has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frameworks")
    public ResponseEntity<Framework> createFramework(@Valid @RequestBody Framework framework) throws URISyntaxException {
        log.debug("REST request to save Framework : {}", framework);
        if (framework.getId() != null) {
            throw new BadRequestAlertException("A new framework cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Framework result = frameworkService.save(framework);
        return ResponseEntity
            .created(new URI("/api/frameworks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frameworks/:id} : Updates an existing framework.
     *
     * @param id the id of the framework to save.
     * @param framework the framework to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated framework,
     * or with status {@code 400 (Bad Request)} if the framework is not valid,
     * or with status {@code 500 (Internal Server Error)} if the framework couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frameworks/{id}")
    public ResponseEntity<Framework> updateFramework(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Framework framework
    ) throws URISyntaxException {
        log.debug("REST request to update Framework : {}, {}", id, framework);
        if (framework.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, framework.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frameworkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Framework result = frameworkService.update(framework);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, framework.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frameworks/:id} : Partial updates given fields of an existing framework, field will ignore if it is null
     *
     * @param id the id of the framework to save.
     * @param framework the framework to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated framework,
     * or with status {@code 400 (Bad Request)} if the framework is not valid,
     * or with status {@code 404 (Not Found)} if the framework is not found,
     * or with status {@code 500 (Internal Server Error)} if the framework couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frameworks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Framework> partialUpdateFramework(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Framework framework
    ) throws URISyntaxException {
        log.debug("REST request to partial update Framework partially : {}, {}", id, framework);
        if (framework.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, framework.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frameworkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Framework> result = frameworkService.partialUpdate(framework);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, framework.getId().toString())
        );
    }

    /**
     * {@code GET  /frameworks} : get all the frameworks.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frameworks in body.
     */
    @GetMapping("/frameworks")
    public ResponseEntity<List<Framework>> getAllFrameworks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Frameworks");
        Page<Framework> page;
        if (eagerload) {
            page = frameworkService.findAllWithEagerRelationships(pageable);
        } else {
            page = frameworkService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frameworks/:id} : get the "id" framework.
     *
     * @param id the id of the framework to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the framework, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frameworks/{id}")
    public ResponseEntity<Framework> getFramework(@PathVariable Long id) {
        log.debug("REST request to get Framework : {}", id);
        Optional<Framework> framework = frameworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(framework);
    }

    /**
     * {@code DELETE  /frameworks/:id} : delete the "id" framework.
     *
     * @param id the id of the framework to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frameworks/{id}")
    public ResponseEntity<Void> deleteFramework(@PathVariable Long id) {
        log.debug("REST request to delete Framework : {}", id);
        frameworkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
