package com.example.final_project;

import com.example.final_project.model.entity.StatisticsEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.repository.StatisticsRepository;
import com.example.final_project.repository.UserRepository;
import com.example.final_project.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindStatisticsByUserId() {
        Long userId = 1L;
        List<StatisticsEntity> expectedStatistics = List.of(new StatisticsEntity());
        
        when(statisticsRepository.findStatisticsEntitiesByUserIdEquals(userId)).thenReturn(expectedStatistics);

        List<StatisticsEntity> result = statisticsService.findStatisticsByUserId(userId);

        assertNotNull(result);
        assertEquals(expectedStatistics.size(), result.size());
        verify(statisticsRepository, times(1)).findStatisticsEntitiesByUserIdEquals(userId);
    }

    @Test
    void testFindStatisticsByMusicId() {
        Long musicId = 1L;
        List<StatisticsEntity> expectedStatistics = List.of(new StatisticsEntity());

        when(statisticsRepository.findStatisticsEntitiesByMusicIdEquals(musicId)).thenReturn(expectedStatistics);

        List<StatisticsEntity> result = statisticsService.findStatisticsByMusicId(musicId);

        assertNotNull(result);
        assertEquals(expectedStatistics.size(), result.size());
        verify(statisticsRepository, times(1)).findStatisticsEntitiesByMusicIdEquals(musicId);
    }

    @Test
    void testIncreasePlayCount_RecordExists() {
        StatisticsEntity stat = new StatisticsEntity();
        stat.setPlayCount(5);
        UserEntity user = new UserEntity();
        user.setId(1L);
        stat.setUser(user);

        MusicEntity music = new MusicEntity();
        music.setId(1L);
        stat.setMusic(music);

        Optional<StatisticsEntity> existingStat = Optional.of(stat);

        when(statisticsRepository.findStatisticsEntityByUserIdAndMusicIdEquals(1L, 1L)).thenReturn(existingStat);

        statisticsService.increasePlayCount(stat);

        verify(statisticsRepository, times(1)).saveAndFlush(stat);
        assertEquals(6, stat.getPlayCount());
    }

    @Test
    void testIncreasePlayCount_RecordDoesNotExist() {
        StatisticsEntity stat = new StatisticsEntity();
        stat.setPlayCount(0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        stat.setUser(user);

        MusicEntity music = new MusicEntity();
        music.setId(1L);
        stat.setMusic(music);

        when(statisticsRepository.findStatisticsEntityByUserIdAndMusicIdEquals(1L, 1L)).thenReturn(Optional.empty());

        statisticsService.increasePlayCount(stat);

        verify(statisticsRepository, times(1)).saveAndFlush(stat);
        assertEquals(1, stat.getPlayCount());
    }

    @Test
    void testGeneratePlayCountReport() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setUserStatus(UserStatus.ACTIVE);

        MusicEntity music = new MusicEntity();
        music.setId(1L);
        music.setMusicName("Test Song");
        music.setAuthor(user);

        StatisticsEntity stat = new StatisticsEntity();
        stat.setMusic(music);
        stat.setPlayCount(5);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(statisticsRepository.findStatisticsEntitiesByUserIdEquals(1L)).thenReturn(Collections.singletonList(stat));

        List<StatisticsResponse> result = statisticsService.generatePlayCountReport();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}