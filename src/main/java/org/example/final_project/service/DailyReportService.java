package org.example.final_project.service;

import lombok.RequiredArgsConstructor;
import org.example.final_project.model.entity.DailyReportEntity;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.repository.DailyReportRepository;
import org.example.final_project.repository.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public Page<DailyReportEntity> getAllReports(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return dailyReportRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    @Scheduled(cron = "0 10 16 * * *")
    public void generateDailyReport() {
        List<OrderHistoryEntity> dailyPurchases = orderHistoryRepository
                .findAllByPurchaseDateEquals(LocalDate.now());
        Map<ShopEntity, List<OrderHistoryEntity>> collect = dailyPurchases.stream()
                .collect(Collectors.groupingBy(OrderHistoryEntity::getShop));

        collect.forEach((shopEntity, historyList) -> {
            Double dailySalesForShop = 0.00;
            for (OrderHistoryEntity orderHistoryEntity : historyList) {
                dailySalesForShop += orderHistoryEntity.getTotalPrice();
            }
            dailyReportRepository.save(DailyReportEntity.builder()
                    .shop(shopEntity)
                    .totalSales(dailySalesForShop)
                    .build());
        });
    }
}
