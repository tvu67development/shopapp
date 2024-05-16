package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotBlank(message = "Name must be required")
    @JsonProperty("fullname")
    private String fullName;

    @NotBlank(message = "Phone number must be required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @NotNull(message = "Password not blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "role_id is required")
    @JsonProperty("role_id")
    private Long roleId;
}
