package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Framework;
import com.mycompany.myapp.repository.FrameworkRepository;
import com.mycompany.myapp.service.FrameworkService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Framework}.
 */
@Service
@Transactional
public class FrameworkServiceImpl implements FrameworkService {

    private final Logger log = LoggerFactory.getLogger(FrameworkServiceImpl.class);

    private final FrameworkRepository frameworkRepository;

    public FrameworkServiceImpl(FrameworkRepository frameworkRepository) {
        this.frameworkRepository = frameworkRepository;
    }

    @Override
    public Framework save(Framework framework) {
        log.debug("Request to save Framework : {}", framework);
        return frameworkRepository.save(framework);
    }

    @Override
    public Framework update(Framework framework) {
        log.debug("Request to update Framework : {}", framework);
        return frameworkRepository.save(framework);
    }

    @Override
    public Optional<Framework> partialUpdate(Framework framework) {
        log.debug("Request to partially update Framework : {}", framework);

        return frameworkRepository
            .findById(framework.getId())
            .map(existingFramework -> {
                if (framework.getName() != null) {
                    existingFramework.setName(framework.getName());
                }
                if (framework.getCreatedDate() != null) {
                    existingFramework.setCreatedDate(framework.getCreatedDate());
                }
                if (framework.getModifiedDate() != null) {
                    existingFramework.setModifiedDate(framework.getModifiedDate());
                }

                return existingFramework;
            })
            .map(frameworkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Framework> findAll(Pageable pageable) {
        log.debug("Request to get all Frameworks");
        return frameworkRepository.findAll(pageable);
    }

    public Page<Framework> findAllWithEagerRelationships(Pageable pageable) {
        return frameworkRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Framework> findOne(Long id) {
        log.debug("Request to get Framework : {}", id);
        return frameworkRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Framework : {}", id);
        frameworkRepository.deleteById(id);
    }
}
