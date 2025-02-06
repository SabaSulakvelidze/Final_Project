package org.example.final_project.service;

import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.OutOfStockException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Page<StockEntity> getAllStocks(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return stockRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public StockEntity addProductToShop(StockEntity stockEntity) {
        StockEntity stockByShopIdAndProductId = getStockByShopIdAndProductId(stockEntity.getShop().getId(), stockEntity.getProduct().getId());
        if (stockByShopIdAndProductId != null) throw new RuntimeException("stock with shopId %d and productId %d already exists".formatted(stockEntity.getShop().getId(), stockEntity.getProduct().getId()));
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
            throw new OutOfStockException("you're trying to purchase %d item(s), item(s) in stock; %d".formatted(desiredQuantity, stock.getQuantity()));
        return stock;
    }
}
