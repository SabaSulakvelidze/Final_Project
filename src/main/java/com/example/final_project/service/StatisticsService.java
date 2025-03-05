package com.example.final_project.service;

import com.example.final_project.model.entity.StatisticsEntity;
import com.example.final_project.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public List<StatisticsEntity> findStatisticsByUserId(Long userId) {
        return statisticsRepository.findStatisticsEntitiesByUserIdEquals(userId);
    }

    public List<StatisticsEntity> findStatisticsByMusicId(Long musicId) {
        return statisticsRepository.findStatisticsEntitiesByMusicIdEquals(musicId);
    }

    @Transactional
    public void increasePlayCount(StatisticsEntity stat) {
        Optional<StatisticsEntity> statOption = statisticsRepository.findStatisticsEntityByUserIdAndMusicIdEquals(stat.getUser().getId(), stat.getMusic().getId());
        if (statOption.isPresent()) {
            statOption.get().setPlayCount(statOption.get().getPlayCount() + 1);
            statisticsRepository.saveAndFlush(statOption.get());
        }else{
            stat.setPlayCount(1);
            statisticsRepository.saveAndFlush(stat);
        }
    }

}
