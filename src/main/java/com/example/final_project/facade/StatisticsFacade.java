package com.example.final_project.facade;

import com.example.final_project.model.entity.StatisticsEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.model.response.statistics.PlayCountStat;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.service.StatisticsService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatisticsFacade {
    private final StatisticsService statisticsService;
    private final UserService userService;

    @Scheduled(cron = "0 0 18 * * 5")
    public List<StatisticsResponse> generateWeeklyReport() {
        List<StatisticsResponse> result = new ArrayList<>();

        userService.getAllUsers().stream()
                .filter(user -> user.getUserStatus() == UserStatus.ACTIVE)
                .forEach(user -> {
                    Set<PlayCountStat> playCountStats = new HashSet<>();
                    statisticsService.findStatisticsByUserId(user.getId()).forEach(stat -> {
                        playCountStats.add(PlayCountStat.builder()
                                .musicId(stat.getMusic().getId())
                                .musicName(stat.getMusic().getMusicName())
                                .playCount(stat.getPlayCount())
                                .build());
                    });
                    result.add(StatisticsResponse.builder()
                            .user(UserResponse.toUserResponse(user))
                            .playCountStats(playCountStats)
                            .build());
                });
        System.out.println("=====================");
        result.forEach(System.out::println);
        System.out.println("=====================");
        return result;
    }
}
