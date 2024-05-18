package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {

    @NotBlank(message = "Phone number must be required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull(message = "Password not blank")
    private String password;

    @JsonProperty("role_id")
    private Long roleId;
}
