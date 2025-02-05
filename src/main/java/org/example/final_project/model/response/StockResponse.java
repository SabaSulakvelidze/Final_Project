package org.example.final_project.model.response;

import lombok.*;
import org.example.final_project.model.ProductShopId;
import org.example.final_project.model.entity.StockEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {
    private ProductShopId productShopId;
    private ProductResponse product;
    private ShopResponse shop;
    private Integer quantity;
    private Double price;

    public static StockResponse toStockResponse(StockEntity stockEntity){
        return StockResponse.builder()
                .productShopId(stockEntity.getProductShopId())
                .product(ProductResponse.builder().id(stockEntity.getProduct().getId()).name(stockEntity.getProduct().getName()).build())
                .shop(ShopResponse.builder().id(stockEntity.getShop().getId()).name(stockEntity.getShop().getName()).build())
                .quantity(stockEntity.getQuantity())
                .price(stockEntity.getPrice())
                .build();
    }
}
