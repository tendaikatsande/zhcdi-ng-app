package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FrameworkType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FrameworkType}.
 */
public interface FrameworkTypeService {
    /**
     * Save a frameworkType.
     *
     * @param frameworkType the entity to save.
     * @return the persisted entity.
     */
    FrameworkType save(FrameworkType frameworkType);

    /**
     * Updates a frameworkType.
     *
     * @param frameworkType the entity to update.
     * @return the persisted entity.
     */
    FrameworkType update(FrameworkType frameworkType);

    /**
     * Partially updates a frameworkType.
     *
     * @param frameworkType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FrameworkType> partialUpdate(FrameworkType frameworkType);

    /**
     * Get all the frameworkTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrameworkType> findAll(Pageable pageable);

    /**
     * Get the "id" frameworkType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FrameworkType> findOne(Long id);

    /**
     * Delete the "id" frameworkType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
