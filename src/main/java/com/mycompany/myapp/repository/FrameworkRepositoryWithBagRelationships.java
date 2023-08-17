package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Framework;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FrameworkRepositoryWithBagRelationships {
    Optional<Framework> fetchBagRelationships(Optional<Framework> framework);

    List<Framework> fetchBagRelationships(List<Framework> frameworks);

    Page<Framework> fetchBagRelationships(Page<Framework> frameworks);
}
