package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//@EqualsAndHashCode(callSuper = true) -- test thứ tự trả về của Response trong Postman
@Entity // thuc the trong JavaSpring
@Table(name = "products")  // ten bang trong DB
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Data -- test thứ tự trả về của Response trong Postman
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Float price;  // float vs Float

    @Column(length = 300)
    private String thumbnail;

    private String description;

    @JoinColumn(name = "category_id")  // ten column co vai tro FOREIGN KEY trong bang
    @ManyToOne // Many thuoc ve Product Entity - One thuoc ve Category -> moi mot product phai thuoc ve 1 Category
    private Category category;

}
