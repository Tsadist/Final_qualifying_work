package com.example.kyrsovay.service;

import com.example.kyrsovay.config.EmployeeUserDetails;
import com.example.kyrsovay.domain.Employee;
import com.example.kyrsovay.repository.EmployeeRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee byUsername = employeeRepo.findByUsername(username);
        if(byUsername == null)
            return null;
        return new EmployeeUserDetails(byUsername);
    }
}
