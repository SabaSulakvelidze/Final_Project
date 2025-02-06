package org.example.final_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.model.response.OrderHistoryResponse;
import org.example.final_project.model.response.StockResponse;
import org.example.final_project.service.OrderHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderHistory")
@RequiredArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<OrderHistoryResponse> getAllOrderHistory(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                         @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                         @RequestParam(defaultValue = "id") String sortBy) {
        return orderHistoryService.getAllOrderHistory(pageNumber, pageSize, direction, sortBy).map(OrderHistoryResponse::toOrderHistoryResponse);
    }

    @GetMapping("/getAllOrders/{buyerId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<OrderHistoryResponse> getOrderByBuyerId(@PathVariable Long buyerId,
                                                      @RequestParam(defaultValue = "0") Integer pageNumber,
                                                      @RequestParam(defaultValue = "5") Integer pageSize,
                                                      @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        return orderHistoryService.getOrderByBuyerId(buyerId, pageNumber, pageSize, direction, sortBy).map(OrderHistoryResponse::toOrderHistoryResponse);
    }

    @GetMapping("/getSingleOrder/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public OrderHistoryResponse getOrderById(@PathVariable Long orderId) {
        return OrderHistoryResponse.toOrderHistoryResponse(orderHistoryService.getOrderById(orderId));
    }
}
