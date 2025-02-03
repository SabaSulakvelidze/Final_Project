package org.example.final_project.model.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.StockEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {
    private Long id;
    private String name;
    private Set<StockEntity> stocks = new HashSet<>();

    public static ShopResponse toShopResponse(ShopEntity shopEntity){
        return ShopResponse.builder()
                .id(shopEntity.getId())
                .name(shopEntity.getName())
                .stocks(shopEntity.getStocks())
                .build();
    }
}
