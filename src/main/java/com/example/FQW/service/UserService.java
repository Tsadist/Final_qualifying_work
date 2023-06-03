package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.controller.utils.Randomizer;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.AuthorizeRequest;
import com.example.FQW.models.request.NewEmployeeRequest;
import com.example.FQW.models.request.ProfileEditRequest;
import com.example.FQW.models.request.RegistrationRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSender mailSender;

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

    public AnswerResponse createCustomer(RegistrationRequest registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()) == null) {
            String activationCode = "";

            User user = new User();
            user.setUserRole(UserRole.CUSTOMER);
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(registrationRequest.getPassword());
            user.setPhoneNumber(registrationRequest.getPhoneNumber());
            userRepo.save(user);
            String message = String
                    .format("Для активации личного кабинета перейдите по ссылке: http://localhost:8020/activate/%s",
                            activationCode);
            mailSender.send(registrationRequest.getEmail(), "Активация аккаунта", message);
            return new AnswerResponse("Пользователь успешно создан, для входа в ЛК нужно подтвердить аккаунт");

        }
        throw new RequestException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
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
            user.setActive(true);
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
