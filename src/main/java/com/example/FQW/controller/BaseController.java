package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.config.security.JwtTokenService;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.LoginRequest;
import com.example.FQW.models.request.RegistrationRequest;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.models.response.LoginResponse;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BaseController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

//    private static User user;

    @GetMapping("/registration")
    public HttpStatus getRegistration() {
        return HttpStatus.OK;
    }

    @PostMapping("/registration")
    public ResponseEntity<MessageResponse> registration(@RequestBody RegistrationRequest newClientRequest) {
        if (userRepo.findByEmail(newClientRequest.getEmail()) == null) {

            User user = new User();
            user.setUserRole(UserRole.CUSTOMER);
            user.setEmail(newClientRequest.getEmail());
            user.setPassword(newClientRequest.getPassword());
            user.setPhoneNumber(newClientRequest.getPhoneNumber());
            userRepo.save(user);
            return ResponseEntity.ok(new MessageResponse("Пользователь создан"));
        }
        throw new RequestException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
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

        User user = userDetails.getClient();

        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getUserRole())
                .build();

        return ResponseEntity.ok(userResponse);
    }


}

