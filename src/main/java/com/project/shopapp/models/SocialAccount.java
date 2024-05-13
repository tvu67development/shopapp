package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity // thuc the trong JavaSpring
@Table(name = "social_accounts")  // ten bang trong DB
@Data //toString()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 50)
    private String providerId;

    @Column(length = 150)
    private String email;

    @Column(length = 100)
    private String name;

    @JoinColumn(name = "user_id")
    @ManyToOne  // moi mot Social Account se ung voi mot User
    private User user;
}
