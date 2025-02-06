package org.example.final_project.model.response;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.final_project.model.entity.OrderHistoryEntity;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryResponse {
    private Long id;
    private UserResponse buyer;
    private ProductResponse product;
    private ShopResponse shop;
    private Integer quantityBought;
    private Double totalPrice;
    private LocalDate purchaseDate;

    public static OrderHistoryResponse toOrderHistoryResponse(OrderHistoryEntity orderHistoryEntity){
        return OrderHistoryResponse.builder()
                .id(orderHistoryEntity.getId())
                .buyer(UserResponse.toUserResponse(orderHistoryEntity.getBuyer()))
                .product(ProductResponse.toProductResponse(orderHistoryEntity.getProduct()))
                .shop(ShopResponse.toShopResponse(orderHistoryEntity.getShop()))
                .quantityBought(orderHistoryEntity.getQuantityBought())
                .totalPrice(orderHistoryEntity.getTotalPrice())
                .build();
    }
}
