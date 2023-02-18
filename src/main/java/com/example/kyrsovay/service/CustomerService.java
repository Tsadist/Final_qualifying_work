package com.example.kyrsovay.service;

import com.example.kyrsovay.config.CustomerUserDetails;
import com.example.kyrsovay.domain.Customer;
import com.example.kyrsovay.repository.CustomerRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer byUsername = customerRepo.findByUsername(username);
        if(byUsername == null)
            return null;
        return new CustomerUserDetails(byUsername);
    }

}
