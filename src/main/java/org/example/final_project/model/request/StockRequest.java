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
    private Long productId;
    private Long shopId;
    private Integer quantity;
    private Double price;
}
