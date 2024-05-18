package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4300")
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody UserDTO userDTO,
                                                     BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        RegisterResponse.builder()
                                .message(errorMessages.toString())
                                .user(new User())
                                .build()
                );
            }
            if (!userDTO.getRetypePassword().equals(userDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        RegisterResponse.builder()
                                .message(localizationUtils.getLocalizeMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .user(new User())
                                .build()
                );
            }
            User user = iUserService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    RegisterResponse.builder()
                            .message(localizationUtils.getLocalizeMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RegisterResponse.builder()
                            .message(e.getMessage())
                            .user(new User())
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                               BindingResult result) {
        try {
            String token = iUserService.login(
                    userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(), userLoginDTO.getRoleId());

            return ResponseEntity.status(HttpStatus.OK).body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizeMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizeMessage(MessageKeys.LOGIN_FAILED,e.getMessage()))
                            .token("")
                            .build()
            );
        }
    }
}
