package com.example.final_project.repository;

import com.example.final_project.model.entity.MusicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Long>, JpaSpecificationExecutor<MusicEntity> {

    Page<MusicEntity> findMusicEntitiesByMusicNameContaining(String musicName, Pageable pageable);

}
