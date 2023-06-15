package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.controller.utils.Randomizer;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.*;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.UserResponse;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final CustomMailSender mailSender;

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

    public UserResponse getUserResponse(CustomUserDetails userDetails) {
        return getUserResponse(userDetails.getClient());
    }

    public List<UserResponse> getAllEmployee() {
        return userRepo.findAllByUserRoleNot(UserRole.CUSTOMER).stream().map(this::getUserResponse).toList();
    }

    public UserResponse editUser(CustomUserDetails userDetails, ProfileEditRequest profileEditRequest) {
        User user = userDetails.getClient();
        String name = profileEditRequest.getName();
        String surname = profileEditRequest.getSurname();
        String phoneNumber = profileEditRequest.getPhoneNumber();
        if (name != null) {
            user.setName(name);
        }
        if (surname != null) {
            user.setSurname(surname);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        return getUserResponse(userRepo.save(user));
    }

    public UserResponse editAuthorizeDate(CustomUserDetails userDetails, AuthorizeRequest authorizeRequest) {
        User user = userDetails.getClient();
        String password = authorizeRequest.getPassword();
        String email = authorizeRequest.getEmail();
        if (password != null && email != null) {
            user.setPassword(password);
            user.setEmail(email);
        }
        return getUserResponse(userRepo.save(user));
    }

    public AnswerResponse createCustomer(RegistrationRequest registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()) == null) {
            String activationCode = Randomizer.getRandomString(16);

            userRepo.save(User
                    .builder()
                    .userRole(UserRole.CUSTOMER)
                    .email(registrationRequest.getEmail())
                    .password(registrationRequest.getPassword())
                    .activationCode(activationCode)
                    .active(false)
                    .build());

            String message = String
                    .format("Для активации личного кабинета перейдите по ссылке: http://localhost:8080/registration/?t=%s",
                            activationCode);
            mailSender.send(registrationRequest.getEmail(), "Активация аккаунта", message);
            return new AnswerResponse("Пользователь успешно создан, для входа в ЛК нужно подтвердить аккаунт");

        }
        throw new RequestException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
    }

    public UserResponse createEmployee(NewEmployeeRequest newEmployeeRequest) {
        if (userRepo.findByEmail(newEmployeeRequest.getEmail()) == null
                && isUserRoleBelongsEmployee(newEmployeeRequest.getUserRole())) {
            String password = Randomizer.getRandomString();
            mailSender.send(newEmployeeRequest.getEmail(), "Данные для входа в личный кабинет", String
                    .format("Пароль от вашего личного кабинета: %s", password));
            return getUserResponse(userRepo.save(User
                    .builder()
                    .phoneNumber(newEmployeeRequest.getPhoneNumber())
                    .userRole(newEmployeeRequest.getUserRole())
                    .email(newEmployeeRequest.getEmail())
                    .name(newEmployeeRequest.getName())
                    .surname(newEmployeeRequest.getSurname())
                    .password(password)
                    .active(true)
                    .build()));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Сотрудник с таким email уже существует");
        }
    }

    public AnswerResponse accountActivate(ActivateCodeRequest activationCodeRequest) {
        User user = userRepo.findByActivationCode(activationCodeRequest.getActivationCode());

        if (user == null) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Данный пользователь не регистрировался");
        } else {
            user.setActivationCode(null);
            user.setActive(true);
            userRepo.save(user);
            return new AnswerResponse("Аккаунт успешно активирован");
        }

    }

    protected User getUser(Long userId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Пользователь с таким Id не найден");

        return userRepo
                .findById(userId)
                .orElseThrow(requestExceptionSupplier);
    }

    protected List<User> getAllUser(List<Long> userIds) {
        return userRepo
                .findAllById(userIds);
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

    private boolean isUserRoleBelongsEmployee(UserRole role) {
        if (role == UserRole.CUSTOMER)
            throw new RequestException(HttpStatus.BAD_REQUEST, "Роль пользователя должна соответствовать роли сотрудника");

        return true;
    }
}
