package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.User;
import com.example.FQW.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User byEmail = userRepo.findByEmail(email);
        if (byEmail != null) {
            return new CustomUserDetails(byEmail);
        } else {
            log.error("Не найден Client в БД");
            throw new UsernameNotFoundException("NOOOOOOO");
        }
    }

}
