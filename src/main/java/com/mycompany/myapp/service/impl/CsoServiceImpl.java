package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cso;
import com.mycompany.myapp.repository.CsoRepository;
import com.mycompany.myapp.service.CsoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cso}.
 */
@Service
@Transactional
public class CsoServiceImpl implements CsoService {

    private final Logger log = LoggerFactory.getLogger(CsoServiceImpl.class);

    private final CsoRepository csoRepository;

    public CsoServiceImpl(CsoRepository csoRepository) {
        this.csoRepository = csoRepository;
    }

    @Override
    public Cso save(Cso cso) {
        log.debug("Request to save Cso : {}", cso);
        return csoRepository.save(cso);
    }

    @Override
    public Cso update(Cso cso) {
        log.debug("Request to update Cso : {}", cso);
        return csoRepository.save(cso);
    }

    @Override
    public Optional<Cso> partialUpdate(Cso cso) {
        log.debug("Request to partially update Cso : {}", cso);

        return csoRepository
            .findById(cso.getId())
            .map(existingCso -> {
                if (cso.getFirstName() != null) {
                    existingCso.setFirstName(cso.getFirstName());
                }
                if (cso.getLastName() != null) {
                    existingCso.setLastName(cso.getLastName());
                }
                if (cso.getOrganisation() != null) {
                    existingCso.setOrganisation(cso.getOrganisation());
                }
                if (cso.getCell() != null) {
                    existingCso.setCell(cso.getCell());
                }
                if (cso.getCity() != null) {
                    existingCso.setCity(cso.getCity());
                }
                if (cso.getEmail() != null) {
                    existingCso.setEmail(cso.getEmail());
                }
                if (cso.getRegistrationCertificate() != null) {
                    existingCso.setRegistrationCertificate(cso.getRegistrationCertificate());
                }
                if (cso.getOrganisationProfile() != null) {
                    existingCso.setOrganisationProfile(cso.getOrganisationProfile());
                }
                if (cso.getManagementStructure() != null) {
                    existingCso.setManagementStructure(cso.getManagementStructure());
                }
                if (cso.getStrategicPlan() != null) {
                    existingCso.setStrategicPlan(cso.getStrategicPlan());
                }
                if (cso.getResourceMobilisationPlan() != null) {
                    existingCso.setResourceMobilisationPlan(cso.getResourceMobilisationPlan());
                }
                if (cso.getComments() != null) {
                    existingCso.setComments(cso.getComments());
                }
                if (cso.getEnquiries() != null) {
                    existingCso.setEnquiries(cso.getEnquiries());
                }
                if (cso.getLat() != null) {
                    existingCso.setLat(cso.getLat());
                }
                if (cso.getLng() != null) {
                    existingCso.setLng(cso.getLng());
                }
                if (cso.getCreatedDate() != null) {
                    existingCso.setCreatedDate(cso.getCreatedDate());
                }
                if (cso.getModifiedDate() != null) {
                    existingCso.setModifiedDate(cso.getModifiedDate());
                }

                return existingCso;
            })
            .map(csoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cso> findAll(Pageable pageable) {
        log.debug("Request to get all Csos");
        return csoRepository.findAll(pageable);
    }

    public Page<Cso> findAllWithEagerRelationships(Pageable pageable) {
        return csoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cso> findOne(Long id) {
        log.debug("Request to get Cso : {}", id);
        return csoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cso : {}", id);
        csoRepository.deleteById(id);
    }
}
