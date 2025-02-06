package org.example.final_project.service;

import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.repository.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryEntity createOrderHistory(OrderHistoryEntity order){
        return orderHistoryRepository.save(order);
    }

    public Page<OrderHistoryEntity> getAllOrderHistory(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy){
        return orderHistoryRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public OrderHistoryEntity getOrderById(Long orderId){
        return orderHistoryRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("order with id %d was not found".formatted(orderId)));
    }

    public Page<OrderHistoryEntity> getOrderByBuyerId(Long buyerId,Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy){
        return orderHistoryRepository.findAllByBuyerId(buyerId,PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }
}
