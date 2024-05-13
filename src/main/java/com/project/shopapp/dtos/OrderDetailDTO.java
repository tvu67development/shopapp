package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

//    private Long id;  -- dung cho mapper cua DTO sang Model

    @Min(value = 1, message = "Order ID must be > 0")
    @JsonProperty("order_id")
    private Long orderId;

    @Min(value = 1, message = "Product ID must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 1, message = "Price must be > 0")
    private Float price;

    @Min(value = 1, message = "Number of products must be greater than or equal 1")
    @JsonProperty("number_of_products")
    private Integer numberOfProducts;

    @Min(value = 0, message = "Total money must be greater than or equal 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;
}
