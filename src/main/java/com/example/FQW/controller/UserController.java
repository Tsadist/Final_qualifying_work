package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.config.security.JwtTokenService;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.*;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.models.response.LoginResponse;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.repository.UserRepo;
import com.example.FQW.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/registration")
    public ResponseEntity<AnswerResponse> registration(@RequestBody RegistrationRequest newClientRequest) {
        if (userRepo.findByEmail(newClientRequest.getEmail()) == null) {

            User user = new User();
            user.setUserRole(UserRole.CUSTOMER);
            user.setEmail(newClientRequest.getEmail());
            user.setPassword(newClientRequest.getPassword());
            user.setPhoneNumber(newClientRequest.getPhoneNumber());
            userRepo.save(user);
            return ResponseEntity.ok(new AnswerResponse("Пользователь создан"));
        }
        throw new RequestException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
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
        }

        loginResponse.setToken(jwtTokenService.createToken(user));
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getProfile(userDetails));
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<UserResponse> editProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @RequestBody ProfileEditRequest profileEditRequest) {
        return ResponseEntity.ok(userService.editProfile(userDetails, profileEditRequest));
    }

    @PutMapping("/profile/authorize/edit")
    public ResponseEntity<UserResponse> editAuthorizeDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody AuthorizeRequest authorizeRequest) {
        return ResponseEntity.ok(userService.editAuthorizeDate(userDetails, authorizeRequest));
    }
}