package org.example.final_project.model.response;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.entity.DailyReportEntity;
import org.example.final_project.model.entity.ShopEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyReportResponse {
    private Long id;
    private Long shopId;
    private Double totalSales;

    public static DailyReportResponse toDailyReportResponse(DailyReportEntity dailyReportEntity){
        return DailyReportResponse.builder()
                .id(dailyReportEntity.getId())
                .shopId(dailyReportEntity.getShop().getId())
                .totalSales(dailyReportEntity.getTotalSales())
                .build();
    }
}
