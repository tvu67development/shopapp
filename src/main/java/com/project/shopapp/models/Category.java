package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity // thuc the trong JavaSpring
@Table(name = "categories")  // ten bang trong DB
@Data //toString()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
