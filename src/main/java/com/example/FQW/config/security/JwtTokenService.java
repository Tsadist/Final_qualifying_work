package com.example.FQW.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.FQW.models.DB.User;
import com.example.FQW.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenService {

    private final static String secretKey = "mySecretKey1324";
    private final static Algorithm ALGORITHM = Algorithm.HMAC256(secretKey);

    private final UserService clientSecurityService;

    public JwtTokenService(UserService clientSecurityService) {
        this.clientSecurityService = clientSecurityService;
    }

    public String createToken(User user){
        return JWT
                .create()
                .withSubject(user.getEmail())
                .sign(ALGORITHM);
    }

    public UserDetails parseToken(String token){
        DecodedJWT decode = JWT.decode(token);
        return clientSecurityService.loadUserByUsername(decode.getSubject());
    }
}
