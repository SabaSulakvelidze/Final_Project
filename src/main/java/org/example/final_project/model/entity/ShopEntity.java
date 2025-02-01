package org.example.final_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "shops")
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shopName;

/*    @ManyToOne
    @JoinColumn(name = "shop_owner_id")
    private UserEntity shopOwner;*/


}



