package org.example.final_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.final_project.model.entity.DailyReportEntity;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.repository.DailyReportRepository;
import org.example.final_project.repository.OrderHistoryRepository;
import org.example.final_project.service.DailyReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class DailyReportServiceTests {

    @Mock
    private DailyReportRepository dailyReportRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private DailyReportService dailyReportService;

    private OrderHistoryEntity orderHistory;
    private ShopEntity shop;

    @BeforeEach
    void setUp() {
        shop = new ShopEntity();
        shop.setId(1L);

        orderHistory = new OrderHistoryEntity();
        orderHistory.setId(1L);
        orderHistory.setPurchaseDate(LocalDate.now());
        orderHistory.setShop(shop);
        orderHistory.setTotalPrice(100.0);
    }

    @Test
    void getAllReportsShouldReturnPagedReports() {
        Page<DailyReportEntity> reportPage = new PageImpl<>(List.of(new DailyReportEntity()));
        when(dailyReportRepository.findAll(any(PageRequest.class))).thenReturn(reportPage);

        Page<DailyReportEntity> result = dailyReportService.getAllReports(0, 5, Sort.Direction.ASC, "id");
        assertEquals(1, result.getContent().size());
    }

    @Test
    void generateDailyReportShouldSaveReports() {
        when(orderHistoryRepository.findAllByPurchaseDateEquals(LocalDate.now()))
                .thenReturn(List.of(orderHistory));

        dailyReportService.generateDailyReport();

        verify(dailyReportRepository, times(1)).save(any(DailyReportEntity.class));
    }
}

