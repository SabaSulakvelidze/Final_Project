package org.example.final_project.repository;

import org.example.final_project.model.entity.DailyReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReportEntity,Long> {
}
