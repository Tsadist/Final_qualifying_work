package com.example.FQW.repository;

import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findAllByUserRoleNot(UserRole userRole);

    User findByActivationCode(String activationCode);
}
