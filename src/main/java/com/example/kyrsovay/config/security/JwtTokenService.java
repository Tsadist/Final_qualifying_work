package com.example.kyrsovay.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kyrsovay.models.Client;
import com.example.kyrsovay.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenService {

    private final static String secretKey = "mySecretKey1324";
    private final static Algorithm ALGORITHM = Algorithm.HMAC256(secretKey);

    @Autowired
    private ClientService clientSecurityService;

    public String createToken(Client client){
        return JWT
                .create()
                .withSubject(client.getEmail())
                .sign(ALGORITHM);
    }

    public UserDetails parseToken(String token){
        DecodedJWT decode = JWT.decode(token);
        return clientSecurityService.loadUserByUsername(decode.getSubject());
    }
}
