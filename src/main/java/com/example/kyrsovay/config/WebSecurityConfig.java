package com.example.kyrsovay.config;

import com.example.kyrsovay.repository.EmployeeRepo;
import com.example.kyrsovay.service.CustomerService;
import com.example.kyrsovay.service.EmployeeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final EncryptionConfig encryptionConfig;

    public WebSecurityConfig(CustomerService customerService,
                             EmployeeService employeeService,
                             EncryptionConfig encryptionConfig) {
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.encryptionConfig = encryptionConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/profile")
//                .defaultSuccessUrl("/profile", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService)
                .passwordEncoder(encryptionConfig.getPasswordEncoder());
        auth.userDetailsService(employeeService)
                .passwordEncoder(encryptionConfig.getPasswordEncoder());
    }
}