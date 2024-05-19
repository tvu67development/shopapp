package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {

    private long id;

    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;

    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse newProductResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages())
                .build();
        newProductResponse.setCreatedAt(product.getCreatedAt());
        newProductResponse.setUpdatedAt(product.getUpdatedAt());
        return newProductResponse;
    }
}
