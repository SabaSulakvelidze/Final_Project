package com.example.final_project.controller;

import com.example.final_project.facade.StatisticsFacade;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsFacade statisticsFacade;

    @GetMapping("/getStatistics")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<StatisticsResponse>> getStatistics() {
        return ResponseEntity.ok().body(statisticsFacade.generateWeeklyReport());
    }
}
