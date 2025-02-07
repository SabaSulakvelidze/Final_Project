package org.example.final_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.repository.OrderHistoryRepository;
import org.example.final_project.service.OrderHistoryService;
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
class OrderHistoryServiceTests {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private OrderHistoryService orderHistoryService;

    private OrderHistoryEntity orderHistory;

    @BeforeEach
    void setUp() {
        orderHistory = new OrderHistoryEntity();
        orderHistory.setId(1L);
        orderHistory.setPurchaseDate(LocalDate.now());
    }

    @Test
    void createOrderHistoryShouldSaveAndReturnOrderHistory() {
        when(orderHistoryRepository.save(orderHistory)).thenReturn(orderHistory);

        OrderHistoryEntity result = orderHistoryService.createOrderHistory(orderHistory);
        assertEquals(orderHistory, result);
    }

    @Test
    void getAllOrderHistoryShouldReturnPagedOrders() {
        Page<OrderHistoryEntity> orderPage = new PageImpl<>(List.of(orderHistory));
        when(orderHistoryRepository.findAll(any(PageRequest.class))).thenReturn(orderPage);

        Page<OrderHistoryEntity> result = orderHistoryService.getAllOrderHistory(0, 5, Sort.Direction.ASC, "id");
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getOrderByIdShouldReturnOrderWhenOrderExists() {
        when(orderHistoryRepository.findById(1L)).thenReturn(Optional.of(orderHistory));

        OrderHistoryEntity result = orderHistoryService.getOrderById(1L);
        assertEquals(orderHistory, result);
    }

    @Test
    void getOrderByIdShouldThrowExceptionWhenOrderNotFound() {
        when(orderHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderHistoryService.getOrderById(1L));
    }

    @Test
    void getOrderByBuyerIdShouldReturnPagedOrders() {
        Page<OrderHistoryEntity> orderPage = new PageImpl<>(List.of(orderHistory));
        when(orderHistoryRepository.findAllByBuyerId(eq(1L), any(PageRequest.class))).thenReturn(orderPage);

        Page<OrderHistoryEntity> result = orderHistoryService.getOrderByBuyerId(1L, 0, 5, Sort.Direction.ASC, "id");
        assertEquals(1, result.getContent().size());
    }
}

