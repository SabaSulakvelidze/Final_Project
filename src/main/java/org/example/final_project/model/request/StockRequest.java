package org.example.final_project.model.request;

import lombok.*;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequest {
    private ProductEntity product;
    private ShopEntity shop;
    private Integer quantity;
    private Double price;
}
