package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cso;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cso}.
 */
public interface CsoService {
    /**
     * Save a cso.
     *
     * @param cso the entity to save.
     * @return the persisted entity.
     */
    Cso save(Cso cso);

    /**
     * Updates a cso.
     *
     * @param cso the entity to update.
     * @return the persisted entity.
     */
    Cso update(Cso cso);

    /**
     * Partially updates a cso.
     *
     * @param cso the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cso> partialUpdate(Cso cso);

    /**
     * Get all the csos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cso> findAll(Pageable pageable);

    /**
     * Get all the csos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cso> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cso> findOne(Long id);

    /**
     * Delete the "id" cso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
