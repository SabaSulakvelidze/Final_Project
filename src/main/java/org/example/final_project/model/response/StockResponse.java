package org.example.final_project.model.response;

import lombok.*;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.StockEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {
    private Long id;
    private ProductEntity product;
    private ShopEntity shop;
    private Integer quantity;
    private Double price;

    public static StockResponse toStockResponse(StockEntity stockEntity){
        return StockResponse.builder()
                .id(stockEntity.getId())
                .product(stockEntity.getProduct())
                .shop(stockEntity.getShop())
                .quantity(stockEntity.getQuantity())
                .price(stockEntity.getPrice())
                .build();
    }
}
