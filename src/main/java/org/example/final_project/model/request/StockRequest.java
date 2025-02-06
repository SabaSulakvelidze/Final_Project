package org.example.final_project.model.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequest {
    private Long productId;
    private Long shopId;
    @PositiveOrZero
    private Integer quantity;
    @PositiveOrZero
    private Double price;
}
