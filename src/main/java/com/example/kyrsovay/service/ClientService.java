package com.example.kyrsovay.service;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.domain.Client;
import com.example.kyrsovay.repository.ClientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService implements UserDetailsService {

    private final ClientRepo clientRepo;

    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client byEmail = clientRepo.findByEmail(email);
        if (byEmail != null) {
            return new ClientUserDetails(byEmail);
        } else {
            log.error("Не найден Client в БД");
            return null;
//            System.out.println("EEEEERORE");
//            throw new UsernameNotFoundException("Не найден Client в БД");
        }
    }

}
