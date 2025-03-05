package com.example.final_project.repository;

import com.example.final_project.model.entity.StatisticsEntity;
import com.example.final_project.model.entity.StatisticsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<StatisticsEntity, StatisticsId>, JpaSpecificationExecutor<StatisticsEntity> {
    List<StatisticsEntity> findStatisticsEntitiesByUserIdEquals(Long user_id);
    List<StatisticsEntity> findStatisticsEntitiesByMusicIdEquals(Long music_id);
    Optional<StatisticsEntity> findStatisticsEntityByUserIdAndMusicIdEquals(Long user_id, Long music_id);
}
