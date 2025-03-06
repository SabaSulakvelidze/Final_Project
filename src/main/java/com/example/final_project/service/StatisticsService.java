package com.example.final_project.service;

import com.example.final_project.model.entity.StatisticsEntity;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.model.response.statistics.PlayCountStat;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.repository.StatisticsRepository;
import com.example.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;

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

    public List<StatisticsResponse> generatePlayCountReport() {
        List<StatisticsResponse> result = new ArrayList<>();

        userRepository.findAll().stream()
                .filter(user -> user.getUserStatus() == UserStatus.ACTIVE)
                .forEach(user -> {
                    Set<PlayCountStat> playCountStats = new HashSet<>();
                    findStatisticsByUserId(user.getId()).forEach(stat -> {
                        playCountStats.add(PlayCountStat.builder()
                                .music(MusicResponse.toMusicResponse(stat.getMusic()))
                                .playCount(stat.getPlayCount())
                                .build());
                    });
                    result.add(StatisticsResponse.builder()
                            .user(UserResponse.toUserResponse(user))
                            .playCountStats(playCountStats)
                            .build());
                });
        return result;
    }

}
