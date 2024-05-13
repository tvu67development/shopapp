package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity // thuc the trong JavaSpring
@Table(name = "product_images")  // ten bang trong DB
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductImage {

    public static final int MAX_IMAGES_PER_PRODUCT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne  // moi mot ProductImage ung voi mot Product
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
