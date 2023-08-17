package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FrameworkType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FrameworkType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrameworkTypeRepository extends JpaRepository<FrameworkType, Long> {}
