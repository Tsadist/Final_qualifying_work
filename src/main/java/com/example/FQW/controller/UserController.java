package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.config.security.JwtTokenService;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.request.*;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.LoginResponse;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.repository.UserRepo;
import com.example.FQW.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;

    @PostMapping("/registration")
    public ResponseEntity<AnswerResponse> registration(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.createCustomer(registrationRequest));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/user/create")
    public ResponseEntity<UserResponse> createEmployee (@RequestBody NewEmployeeRequest newEmployeeRequest) {
        return ResponseEntity.ok(userService.createEmployee(newEmployeeRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginClientRequest) {
        LoginResponse loginResponse = new LoginResponse();

        User user = userRepo.findByEmail(loginClientRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginClientRequest.getPassword(), user.getPassword())) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "Введен неверный логин или пароль");
        } else if (!user.isActive()){
            throw new RequestException(HttpStatus.UNAUTHORIZED, "Вы не активировали аккаунт");
        }

        loginResponse.setToken(jwtTokenService.createToken(user));
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserResponse(userDetails));
    }

    @PostMapping("/activate")
    public ResponseEntity<AnswerResponse> activate(@RequestBody ActivateCodeRequest activationCodeRequest) {
        return ResponseEntity.ok(userService.accountActivate(activationCodeRequest));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getUser() {
        return ResponseEntity.ok(userService.getAllEmployee());
    }

    @PutMapping("/user/edit")
    public ResponseEntity<UserResponse> editUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody ProfileEditRequest profileEditRequest) {
        return ResponseEntity.ok(userService.editUser(userDetails, profileEditRequest));
    }

    @PutMapping("/user/authorize/edit")
    public ResponseEntity<UserResponse> editAuthorizeDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody AuthorizeRequest authorizeRequest) {
        return ResponseEntity.ok(userService.editAuthorizeDate(userDetails, authorizeRequest));
    }
}