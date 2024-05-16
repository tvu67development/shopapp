package com.project.shopapp.responses;

import com.project.shopapp.models.User;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    private String message;

    private User user;
}
