package org.example.final_project.service;

import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.OutOfStockException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.model.entity.UserEntity;
import org.example.final_project.repository.StockRepository;
import org.example.final_project.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final OrderHistoryService orderHistoryService;

    public Page<StockEntity> getAllStocks(Integer pageNumber, Integer pageSize) {
        return stockRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public StockEntity addProductToShop(StockEntity stockEntity) {
        Optional<StockEntity> stockEntitiesByShopIdAndProductId = stockRepository.findStockEntitiesByShopIdAndProductId(stockEntity.getShop().getId(), stockEntity.getProduct().getId());
        if (stockEntitiesByShopIdAndProductId.isPresent())
            throw new RuntimeException("stock with shopId %d and productId %d already exists".formatted(stockEntity.getShop().getId(), stockEntity.getProduct().getId()));
        else return stockRepository.save(stockEntity);
    }

    @Transactional
    public StockEntity editStock(StockEntity stockEntity) {
        StockEntity stockToEdit = getStockByShopIdAndProductId(stockEntity.getProduct().getId(), stockEntity.getShop().getId());
        stockToEdit.setProduct(stockEntity.getProduct());
        stockToEdit.setShop(stockEntity.getShop());
        stockToEdit.setQuantity(stockEntity.getQuantity());
        stockToEdit.setPrice(stockEntity.getPrice());
        return stockToEdit;
    }

    public String deleteStock(Long productId, Long ShopId) {
        stockRepository.delete(getStockByShopIdAndProductId(productId, ShopId));
        return "stock was deleted successfully";
    }


    public Page<StockEntity> getStockByShopId(Long shopId, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return stockRepository.findStockEntitiesByShopId(shopId, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public Page<StockEntity> getStockByProductId(Long productId, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return stockRepository.findStockEntitiesByProductId(productId, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public StockEntity getStockByShopIdAndProductId(Long shopId, Long productId) {
        return stockRepository.findStockEntitiesByShopIdAndProductId(shopId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("stock for shopId %d and productId %d was not found".formatted(shopId, productId)));
    }

    @Transactional
    public StockEntity purchaseProduct(Long shopId, Long productId, Integer desiredQuantity) {
        StockEntity stock = getStockByShopIdAndProductId(shopId, productId);
        if (stock.getQuantity() >= desiredQuantity) stock.setQuantity(stock.getQuantity() - desiredQuantity);
        else
            throw new OutOfStockException("you're trying to purchase %d item(s), item(s) in stock: %d".formatted(desiredQuantity, stock.getQuantity()));
        String buyerName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findFirstByUserNameEquals(buyerName)
                .orElseThrow(() -> new RuntimeException("unknown user: %s".formatted(buyerName)));
        orderHistoryService.createOrderHistory(OrderHistoryEntity.builder()
                .buyer(currentUser)
                .product(stock.getProduct())
                .shop(stock.getShop())
                .quantityBought(desiredQuantity)
                .totalPrice(stock.getPrice() * desiredQuantity)
                .purchaseDate(LocalDate.now())
                .build());
        return stock;
    }
}
