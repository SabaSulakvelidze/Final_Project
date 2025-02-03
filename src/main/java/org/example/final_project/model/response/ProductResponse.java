package org.example.final_project.model.response;

import lombok.*;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.model.request.ProductRequest;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Set<StockEntity> stocks = new HashSet<>();

    public static ProductResponse toProductResponse(ProductEntity productEntity) {
        return new ProductResponse(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getStocks());
    }
}
