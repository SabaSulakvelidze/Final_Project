package org.example.final_project.repository;

import org.example.final_project.model.entity.OrderHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity,Long> {
    Page<OrderHistoryEntity> findAllByBuyerId(Long buyerId,PageRequest pageRequest);
    List<OrderHistoryEntity> findAllByPurchaseDateEquals(LocalDate purchaseDate);
}
