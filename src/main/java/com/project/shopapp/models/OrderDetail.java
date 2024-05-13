package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity // thuc the trong JavaSpring
@Table(name = "order_details")  // ten bang trong DB
@Data //toString()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne  // moi mot OrderDetail ung voi mot Order
    private Order order;

    @JoinColumn(name = "product_id")
    @ManyToOne  // moi mot OrderDetail ung voi mot Product
    private Product product;

    @Column(nullable = false)
    private Float price;

    @Column(name = "number_of_products", nullable = false)
    private Integer numberOfProducts;

    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

    @Column(length = 20)
    private String color;
}
