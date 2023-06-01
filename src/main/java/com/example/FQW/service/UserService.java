package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.controller.utils.Randomizer;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.AuthorizeRequest;
import com.example.FQW.models.request.NewEmployeeRequest;
import com.example.FQW.models.request.ProfileEditRequest;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            throw new UsernameNotFoundException("Не найдет пользователь");
        }
    }

    public UserResponse getProfile(CustomUserDetails userDetails) {
        return getUserResponse(userDetails.getClient());
    }

    public UserResponse editProfile(CustomUserDetails userDetails, ProfileEditRequest profileEditRequest) {
        User user = userDetails.getClient();
        String name = profileEditRequest.getName();
        String surname = profileEditRequest.getSurname();
        String phoneNumber = profileEditRequest.getPhoneNumber();
        if (name != null) {
            user.setName(name);
        } else if (surname != null) {
            user.setSurname(surname);
        } else if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        return getUserResponse(userRepo.save(user));
    }

    public UserResponse editAuthorizeDate(CustomUserDetails userDetails, AuthorizeRequest authorizeRequest) {
        User user = userDetails.getClient();
        String password = authorizeRequest.getPassword();
        String email = authorizeRequest.getEmail();
        if (password != null && isPasswordNew(user, password)) {
            user.setName(password);
        } else if (email != null && isEmailNew(user, email)) {
            user.setSurname(email);
        }
        return getUserResponse(userRepo.save(user));
    }

    public UserResponse createEmployee(NewEmployeeRequest newEmployeeRequest) {
        if (userRepo.findByEmail(newEmployeeRequest.getEmail()) == null
                && isUserRoleBelongsEmployee(newEmployeeRequest.getUserRole())) {
            User user = new User();
            user.setPhoneNumber(newEmployeeRequest.getPhoneNumber());
            user.setUserRole(newEmployeeRequest.getUserRole());
            user.setEmail(newEmployeeRequest.getEmail());
            user.setName(newEmployeeRequest.getName());
            user.setSurname(newEmployeeRequest.getSurname());
            user.setPassword(Randomizer.getRandomString());
            return getUserResponse(userRepo.save(user));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Сотрудник с таким email уже существует");
        }
    }

    private UserResponse getUserResponse(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .surname(user.getSurname())
                .role(user.getUserRole())
                .build();
    }

    private boolean isPasswordNew(User user, String password) {
        if (!Objects.equals(user.getPassword(), password)) {
            return true;
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Новый пароль не может совпадать со старым");
        }
    }

    private boolean isEmailNew(User user, String email) {
        if (!Objects.equals(user.getEmail(), email)) {
            return true;
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Новая почта не может совпадать со старой");
        }
    }

    private boolean isUserRoleBelongsEmployee(UserRole role) {
        switch (role) {
            case CLEANER:
            case MANAGER:
            case MODERATOR:
                return true;
            default:
                throw new RequestException(HttpStatus.BAD_REQUEST, "Роль пользователя должна соответствовать роли сотрудника");
        }
    }
}
