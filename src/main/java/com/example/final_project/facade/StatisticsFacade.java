package com.example.final_project.facade;

import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.model.response.statistics.PlayCountStat;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.service.StatisticsService;
import com.example.final_project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    public void runWeeklyReport() {
        System.out.println("=====================");
        generatePlayCountReport().forEach(System.out::println);
        /*generatePlayCountReport().forEach(stats -> {
            try {
                System.out.println(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(stats));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });*/
        System.out.println("=====================");
    }

    public List<StatisticsResponse> generatePlayCountReport() {
        List<StatisticsResponse> result = new ArrayList<>();

        userService.getAllUsers().stream()
                .filter(user -> user.getUserStatus() == UserStatus.ACTIVE)
                .forEach(user -> {
                    Set<PlayCountStat> playCountStats = new HashSet<>();
                    statisticsService.findStatisticsByUserId(user.getId()).forEach(stat -> {
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
