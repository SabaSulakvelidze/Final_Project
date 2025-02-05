package org.example.final_project.repository;

import org.example.final_project.model.entity.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long> {
    @Query("""
            FROM StockEntity s
            WHERE s.shop.id = :shopId
            """)
    Page<StockEntity> findStockEntitiesByShopId(Long shopId, PageRequest pageRequest);

    @Query("""
            FROM StockEntity s
            WHERE s.product.id = :productId
            """)
    Page<StockEntity> findStockEntitiesByProductId(Long productId, PageRequest pageRequest);

    Optional<StockEntity> findStockEntitiesByShopIdAndProductId(Long shopId,Long productId);
}
