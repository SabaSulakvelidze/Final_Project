package org.example.final_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.request.ProductRequest;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockEntity> stocks = new HashSet<>();

    public static ProductEntity toProductEntity(ProductRequest productRequest){
        return ProductEntity.builder()
                .name(productRequest.getName())
                .stocks(productRequest.getStocks())
                .build();
    }
}
