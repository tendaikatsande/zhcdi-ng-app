package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cso;
import com.mycompany.myapp.repository.CsoRepository;
import com.mycompany.myapp.service.CsoService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Cso}.
 */
@RestController
@RequestMapping("/api")
public class CsoResource {

    private final Logger log = LoggerFactory.getLogger(CsoResource.class);

    private static final String ENTITY_NAME = "cso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CsoService csoService;

    private final CsoRepository csoRepository;

    public CsoResource(CsoService csoService, CsoRepository csoRepository) {
        this.csoService = csoService;
        this.csoRepository = csoRepository;
    }

    /**
     * {@code POST  /csos} : Create a new cso.
     *
     * @param cso the cso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cso, or with status {@code 400 (Bad Request)} if the cso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/csos")
    public ResponseEntity<Cso> createCso(@Valid @RequestBody Cso cso) throws URISyntaxException {
        log.debug("REST request to save Cso : {}", cso);
        if (cso.getId() != null) {
            throw new BadRequestAlertException("A new cso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cso result = csoService.save(cso);
        return ResponseEntity
            .created(new URI("/api/csos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /csos/:id} : Updates an existing cso.
     *
     * @param id the id of the cso to save.
     * @param cso the cso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cso,
     * or with status {@code 400 (Bad Request)} if the cso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/csos/{id}")
    public ResponseEntity<Cso> updateCso(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Cso cso)
        throws URISyntaxException {
        log.debug("REST request to update Cso : {}, {}", id, cso);
        if (cso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!csoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cso result = csoService.update(cso);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cso.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /csos/:id} : Partial updates given fields of an existing cso, field will ignore if it is null
     *
     * @param id the id of the cso to save.
     * @param cso the cso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cso,
     * or with status {@code 400 (Bad Request)} if the cso is not valid,
     * or with status {@code 404 (Not Found)} if the cso is not found,
     * or with status {@code 500 (Internal Server Error)} if the cso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/csos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cso> partialUpdateCso(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Cso cso)
        throws URISyntaxException {
        log.debug("REST request to partial update Cso partially : {}, {}", id, cso);
        if (cso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!csoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cso> result = csoService.partialUpdate(cso);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cso.getId().toString())
        );
    }

    /**
     * {@code GET  /csos} : get all the csos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of csos in body.
     */
    @GetMapping("/csos")
    public ResponseEntity<List<Cso>> getAllCsos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Csos");
        Page<Cso> page;
        if (eagerload) {
            page = csoService.findAllWithEagerRelationships(pageable);
        } else {
            page = csoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /csos/:id} : get the "id" cso.
     *
     * @param id the id of the cso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/csos/{id}")
    public ResponseEntity<Cso> getCso(@PathVariable Long id) {
        log.debug("REST request to get Cso : {}", id);
        Optional<Cso> cso = csoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cso);
    }

    /**
     * {@code DELETE  /csos/:id} : delete the "id" cso.
     *
     * @param id the id of the cso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/csos/{id}")
    public ResponseEntity<Void> deleteCso(@PathVariable Long id) {
        log.debug("REST request to delete Cso : {}", id);
        csoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
