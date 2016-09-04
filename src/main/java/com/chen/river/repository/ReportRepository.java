package com.chen.river.repository;

import com.chen.river.domain.Report;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Report entity.
 */
@SuppressWarnings("unused")
public interface ReportRepository extends JpaRepository<Report,Long> {
    @Query(value = "select report from Report report where isRead=0")
    List<Report> findAllUnreadReports();
}
