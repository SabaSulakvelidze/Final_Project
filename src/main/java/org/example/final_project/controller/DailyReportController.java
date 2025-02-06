package org.example.final_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.final_project.model.response.DailyReportResponse;
import org.example.final_project.model.response.OrderHistoryResponse;
import org.example.final_project.service.DailyReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderHistory")
@RequiredArgsConstructor
public class DailyReportController {

    private final DailyReportService dailyReportService;

    @GetMapping("/getAllReports")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<DailyReportResponse> getAllReports(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                   @RequestParam(defaultValue = "id") String sortBy) {
        return dailyReportService.getAllReports(pageNumber, pageSize, direction, sortBy).map(DailyReportResponse::toDailyReportResponse);
    }
}
