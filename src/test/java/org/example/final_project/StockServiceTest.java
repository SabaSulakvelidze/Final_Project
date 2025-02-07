package org.example.final_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.final_project.exception.OutOfStockException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.ProductShopId;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.model.entity.UserEntity;
import org.example.final_project.repository.StockRepository;
import org.example.final_project.repository.UserRepository;
import org.example.final_project.service.OrderHistoryService;
import org.example.final_project.service.StockService;
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
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderHistoryService orderHistoryService;

    @InjectMocks
    private StockService stockService;

    private StockEntity stock;

    @BeforeEach
    void setUp() {
        stock = new StockEntity();
        stock.setProductShopId(ProductShopId.builder().productId(1L).shopId(1L).build());
        stock.setQuantity(10);
        stock.setPrice(100.0);
    }

    @Test
    void getAllStocksShouldReturnPagedStocks() {
        Page<StockEntity> stockPage = new PageImpl<>(List.of(stock));
        when(stockRepository.findAll(any(PageRequest.class))).thenReturn(stockPage);

        Page<StockEntity> result = stockService.getAllStocks(0, 5);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void deleteStockShouldReturnSuccessMessageWhenStockExists() {
        when(stockRepository.findStockEntitiesByShopIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).delete(stock);

        String result = stockService.deleteStock(1L, 1L);

        assertEquals("stock was deleted successfully", result);
    }

    @Test
    void deleteStockShouldThrowExceptionWhenStockNotFound() {
        when(stockRepository.findStockEntitiesByShopIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stockService.deleteStock(1L, 1L));
    }

    @Test
    void purchaseProductShouldThrowOutOfStockExceptionWhenQuantityNotSufficient() {
        when(stockRepository.findStockEntitiesByShopIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));

        assertThrows(OutOfStockException.class, () -> stockService.purchaseProduct(1L, 1L, 15));
    }
}
