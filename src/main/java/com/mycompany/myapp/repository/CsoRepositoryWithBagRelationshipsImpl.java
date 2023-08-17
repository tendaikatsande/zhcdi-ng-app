package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cso;
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
public class CsoRepositoryWithBagRelationshipsImpl implements CsoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cso> fetchBagRelationships(Optional<Cso> cso) {
        return cso.map(this::fetchProvinces);
    }

    @Override
    public Page<Cso> fetchBagRelationships(Page<Cso> csos) {
        return new PageImpl<>(fetchBagRelationships(csos.getContent()), csos.getPageable(), csos.getTotalElements());
    }

    @Override
    public List<Cso> fetchBagRelationships(List<Cso> csos) {
        return Optional.of(csos).map(this::fetchProvinces).orElse(Collections.emptyList());
    }

    Cso fetchProvinces(Cso result) {
        return entityManager
            .createQuery("select cso from Cso cso left join fetch cso.provinces where cso.id = :id", Cso.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Cso> fetchProvinces(List<Cso> csos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, csos.size()).forEach(index -> order.put(csos.get(index).getId(), index));
        List<Cso> result = entityManager
            .createQuery("select cso from Cso cso left join fetch cso.provinces where cso in :csos", Cso.class)
            .setParameter("csos", csos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
