package com.chen.river.repository;

import com.chen.river.domain.Inspector;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Inspector entity.
 */
@SuppressWarnings("unused")
public interface InspectorRepository extends JpaRepository<Inspector,Long> {

}
