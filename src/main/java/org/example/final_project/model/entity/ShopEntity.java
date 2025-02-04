package org.example.final_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.request.ShopRequest;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "shops")
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockEntity> stocks = new HashSet<>();

    public static ShopEntity toShopEntity(ShopRequest shopRequest){
        return ShopEntity.builder()
                .name(shopRequest.getName())
                .build();
    }
}
