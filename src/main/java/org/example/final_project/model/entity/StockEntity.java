package org.example.final_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.request.StockRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "stocks")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    private Integer quantity;
    private Double price;

    public static StockEntity toStockEntity(StockRequest stockRequest, ProductEntity product, ShopEntity shop) {
        return StockEntity.builder()
                .product(product)
                .shop(shop)
                .quantity(stockRequest.getQuantity())
                .price(stockRequest.getPrice())
                .build();
    }
}
