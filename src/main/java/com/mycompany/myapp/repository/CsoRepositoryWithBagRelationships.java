package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CsoRepositoryWithBagRelationships {
    Optional<Cso> fetchBagRelationships(Optional<Cso> cso);

    List<Cso> fetchBagRelationships(List<Cso> csos);

    Page<Cso> fetchBagRelationships(Page<Cso> csos);
}
