package org.example.final_project.model.request;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.final_project.model.entity.StockEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopRequest {
    private String name;
    private Set<StockEntity> stocks = new HashSet<>();
}
