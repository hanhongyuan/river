package com.chen.river.repository;

import com.chen.river.domain.WorkRoad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkRoad entity.
 */
@SuppressWarnings("unused")
public interface WorkRoadRepository extends JpaRepository<WorkRoad,Long> {

}
