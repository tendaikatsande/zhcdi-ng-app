package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Framework;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FrameworkRepositoryWithBagRelationshipsImpl implements FrameworkRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Framework> fetchBagRelationships(Optional<Framework> framework) {
        return framework.map(this::fetchFrameworkTypes).map(this::fetchFileUploads);
    }

    @Override
    public Page<Framework> fetchBagRelationships(Page<Framework> frameworks) {
        return new PageImpl<>(fetchBagRelationships(frameworks.getContent()), frameworks.getPageable(), frameworks.getTotalElements());
    }

    @Override
    public List<Framework> fetchBagRelationships(List<Framework> frameworks) {
        return Optional.of(frameworks).map(this::fetchFrameworkTypes).map(this::fetchFileUploads).orElse(Collections.emptyList());
    }

    Framework fetchFrameworkTypes(Framework result) {
        return entityManager
            .createQuery(
                "select framework from Framework framework left join fetch framework.frameworkTypes where framework.id = :id",
                Framework.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Framework> fetchFrameworkTypes(List<Framework> frameworks) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, frameworks.size()).forEach(index -> order.put(frameworks.get(index).getId(), index));
        List<Framework> result = entityManager
            .createQuery(
                "select framework from Framework framework left join fetch framework.frameworkTypes where framework in :frameworks",
                Framework.class
            )
            .setParameter("frameworks", frameworks)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Framework fetchFileUploads(Framework result) {
        return entityManager
            .createQuery(
                "select framework from Framework framework left join fetch framework.fileUploads where framework.id = :id",
                Framework.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Framework> fetchFileUploads(List<Framework> frameworks) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, frameworks.size()).forEach(index -> order.put(frameworks.get(index).getId(), index));
        List<Framework> result = entityManager
            .createQuery(
                "select framework from Framework framework left join fetch framework.fileUploads where framework in :frameworks",
                Framework.class
            )
            .setParameter("frameworks", frameworks)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
