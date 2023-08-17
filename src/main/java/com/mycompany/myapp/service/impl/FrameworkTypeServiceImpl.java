package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FrameworkType;
import com.mycompany.myapp.repository.FrameworkTypeRepository;
import com.mycompany.myapp.service.FrameworkTypeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FrameworkType}.
 */
@Service
@Transactional
public class FrameworkTypeServiceImpl implements FrameworkTypeService {

    private final Logger log = LoggerFactory.getLogger(FrameworkTypeServiceImpl.class);

    private final FrameworkTypeRepository frameworkTypeRepository;

    public FrameworkTypeServiceImpl(FrameworkTypeRepository frameworkTypeRepository) {
        this.frameworkTypeRepository = frameworkTypeRepository;
    }

    @Override
    public FrameworkType save(FrameworkType frameworkType) {
        log.debug("Request to save FrameworkType : {}", frameworkType);
        return frameworkTypeRepository.save(frameworkType);
    }

    @Override
    public FrameworkType update(FrameworkType frameworkType) {
        log.debug("Request to update FrameworkType : {}", frameworkType);
        return frameworkTypeRepository.save(frameworkType);
    }

    @Override
    public Optional<FrameworkType> partialUpdate(FrameworkType frameworkType) {
        log.debug("Request to partially update FrameworkType : {}", frameworkType);

        return frameworkTypeRepository
            .findById(frameworkType.getId())
            .map(existingFrameworkType -> {
                if (frameworkType.getName() != null) {
                    existingFrameworkType.setName(frameworkType.getName());
                }
                if (frameworkType.getCreatedDate() != null) {
                    existingFrameworkType.setCreatedDate(frameworkType.getCreatedDate());
                }
                if (frameworkType.getModifiedDate() != null) {
                    existingFrameworkType.setModifiedDate(frameworkType.getModifiedDate());
                }

                return existingFrameworkType;
            })
            .map(frameworkTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrameworkType> findAll(Pageable pageable) {
        log.debug("Request to get all FrameworkTypes");
        return frameworkTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrameworkType> findOne(Long id) {
        log.debug("Request to get FrameworkType : {}", id);
        return frameworkTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrameworkType : {}", id);
        frameworkTypeRepository.deleteById(id);
    }
}
