package org.example.final_project.repository;

import org.example.final_project.model.entity.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long> {

    Page<StockEntity> findStockEntitiesByShopId(Long shopId, PageRequest pageRequest);
    Page<StockEntity> findStockEntitiesByProductId(Long productId, PageRequest pageRequest);
    StockEntity findStockEntitiesByShopIdAndProductId(Long shopId,Long productId);
}
