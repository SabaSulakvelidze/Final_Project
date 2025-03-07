package com.example.final_project;

import com.example.final_project.facade.StatisticsFacade;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsFacadeTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsFacade statisticsFacade;

    @Test
    void runWeeklyReport_shouldCallGeneratePlayCountReport() {
        List<StatisticsResponse> mockReport = List.of(new StatisticsResponse(), new StatisticsResponse());
        when(statisticsService.generatePlayCountReport()).thenReturn(mockReport);
        statisticsFacade.runWeeklyReport();
        verify(statisticsService).generatePlayCountReport();
    }
}
