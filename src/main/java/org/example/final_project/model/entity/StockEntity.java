package org.example.final_project.model.entity;

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
@Table(name = "products")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    private Integer quantity;
    private Double price;

    public static StockEntity toStockEntity(StockRequest stockRequest){
        return StockEntity.builder()
                .product(stockRequest.getProduct())
                .shop(stockRequest.getShop())
                .quantity(stockRequest.getQuantity())
                .price(stockRequest.getPrice())
                .build();
    }
}
