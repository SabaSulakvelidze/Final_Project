package org.example.final_project.Service;

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

    public StockEntity getStockById(Long stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("stock with id %d was not found".formatted(stockId)));
    }

    public StockEntity addProductToShop(StockEntity stockEntity) {
        return stockRepository.save(stockEntity);
    }

    @Transactional
    public StockEntity editStock(Long stockId, StockEntity stockEntity) {
        StockEntity stockToEdit = getStockById(stockId);
        stockToEdit.setProduct(stockEntity.getProduct());
        stockToEdit.setShop(stockEntity.getShop());
        stockToEdit.setQuantity(stockEntity.getQuantity());
        stockToEdit.setPrice(stockEntity.getPrice());
        return stockToEdit;
    }

    public String deleteStock(Long stockId) {
        stockRepository.delete(getStockById(stockId));
        return "stock with id %d was deleted successfully".formatted(stockId);
    }



    public Page<StockEntity> getStockByShopId(Long shopId, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return stockRepository.findStockEntitiesByShopId(shopId, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public Page<StockEntity> getStockByProductId(Long productId, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return stockRepository.findStockEntitiesByProductId(productId, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public StockEntity getStockByShopIdAndProductId(Long shopId, Long productId) {
        return stockRepository.findStockEntitiesByShopIdAndProductId(shopId, productId);
    }

    @Transactional
    public StockEntity purchaseProduct(Long shopId, Long productId, Integer desiredQuantity) {
        StockEntity stock = getStockByShopIdAndProductId(shopId, productId);
        if (stock.getQuantity() >= desiredQuantity) stock.setQuantity(stock.getQuantity() - desiredQuantity);
        else throw new OutOfStockException("you're trying to purchase %d item(s), item(s) in stock; %d".formatted(desiredQuantity,stock.getQuantity()));
        return stock;
    }
}
