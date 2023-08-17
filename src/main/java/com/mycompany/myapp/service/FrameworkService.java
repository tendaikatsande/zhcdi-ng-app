package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Framework;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Framework}.
 */
public interface FrameworkService {
    /**
     * Save a framework.
     *
     * @param framework the entity to save.
     * @return the persisted entity.
     */
    Framework save(Framework framework);

    /**
     * Updates a framework.
     *
     * @param framework the entity to update.
     * @return the persisted entity.
     */
    Framework update(Framework framework);

    /**
     * Partially updates a framework.
     *
     * @param framework the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Framework> partialUpdate(Framework framework);

    /**
     * Get all the frameworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Framework> findAll(Pageable pageable);

    /**
     * Get all the frameworks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Framework> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" framework.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Framework> findOne(Long id);

    /**
     * Delete the "id" framework.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
