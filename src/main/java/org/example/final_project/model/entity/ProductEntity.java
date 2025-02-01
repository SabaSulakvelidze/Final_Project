package org.example.final_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false, unique = true)
    private String manufacturer;

    @Column(nullable = false, unique = true)
    private LocalDate manufactureDate;
}
