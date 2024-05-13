package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // thuc the trong JavaSpring
@Table(name = "tokens")  // ten bang trong DB
@Data //toString()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column
    private boolean revoked;

    @Column
    private boolean expired;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
