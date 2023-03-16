package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.config.security.JwtTokenService;
import com.example.kyrsovay.ex.RequestException;
import com.example.kyrsovay.models.Client;
import com.example.kyrsovay.models.Order;
import com.example.kyrsovay.models.enums.ClientRole;
import com.example.kyrsovay.models.request.LoginRequest;
import com.example.kyrsovay.models.request.RegistrationRequest;
import com.example.kyrsovay.models.response.CleanerResponse;
import com.example.kyrsovay.models.response.ClientResponse;
import com.example.kyrsovay.models.response.LoginResponse;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.ClientRepo;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.service.ManagerService;
import com.example.kyrsovay.service.TimeToString;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BaseController {

    private final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    private final OrderRepo orderRepo;
    private final TimeToString timeToString;
    private final ManagerService managerService;
    private static Client client;


    @GetMapping("/registration")
    public HttpStatus getRegistration() {
        return HttpStatus.OK;
    }

    @PostMapping("/registration")
    public ResponseEntity login(@RequestBody RegistrationRequest newClientRequest) {
        if (clientRepo.findByEmail(newClientRequest.getEmail()) == null) {

            client = new Client();

            client.setClientRole(ClientRole.CUSTOMER);
            client.setEmail(newClientRequest.getEmail());
            client.setPassword(newClientRequest.getPassword());
            client.setPhoneNumber(newClientRequest.getPhoneNumber());
            clientRepo.save(client);
            return ResponseEntity.ok("Пользователь создан");
        }
        throw new RequestException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginClientRequest) {

        LoginResponse loginResponse = new LoginResponse();

        client = clientRepo.findByEmail(loginClientRequest.getEmail());
        if (client == null || !passwordEncoder.matches(loginClientRequest.getPassword(), client.getPassword())) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "Введен неверный логин или пароль");
        }

        loginResponse.setToken(jwtTokenService.createToken(client));

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientResponse> getProfile(@AuthenticationPrincipal ClientUserDetails userDetails) {

        client = userDetails.getClient();

        ClientResponse clientResponse = ClientResponse
                .builder()
                .id(client.getId())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .role(client.getClientRole())
                .build();

        return ResponseEntity.ok(clientResponse);
    }


}

