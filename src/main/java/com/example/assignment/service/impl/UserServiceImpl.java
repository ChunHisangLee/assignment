package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.assignment.constants.ErrorMessages.*;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users registerUser(Users users) {
        if (usersRepository.findByEmail(users.getEmail()).isPresent()) {
            throw new CustomErrorException(
                    HttpStatus.CONFLICT.value(),
                    CONFLICT_STATUS,
                    EMAIL_ALREADY_REGISTERED,
                    POST_USER_API_PATH
            );
        }

        // Encode the user's password
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        // Save the user and cascade save the wallet
        return usersRepository.save(users);
    }

    @Override
    public Optional<Users> updateUser(Long id, Users users) {
        Users existingUser = usersRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.NOT_FOUND.value(),
                NOT_FOUND_STATUS,
                USER_NOT_FOUND,
                PUT_USER_API_PATH + id
        ));

        if (usersRepository.findByEmail(users.getEmail()).filter(user -> !user.getId().equals(id)).isPresent()) {
            throw new CustomErrorException(
                    HttpStatus.CONFLICT.value(),
                    CONFLICT_STATUS,
                    EMAIL_ALREADY_REGISTERED_BY_ANOTHER_USER,
                    PUT_USER_API_PATH + id
            );
        }

        existingUser.setName(users.getName());
        existingUser.setEmail(users.getEmail());

        if (users.getPassword() != null && !users.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(users.getPassword()));
        }

        return Optional.of(usersRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.NOT_FOUND.value(),
                NOT_FOUND_STATUS,
                USER_NOT_FOUND,
                DELETE_USER_API_PATH + id
        ));
        usersRepository.delete(user);
    }

    @Override
    public Users login(String email, String password) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new CustomErrorException(
                HttpStatus.UNAUTHORIZED.value(),
                UNAUTHORIZED_STATUS,
                INVALID_EMAIL_OR_PASSWORD,
                POST_LOGIN_API_PATH
        ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomErrorException(
                    HttpStatus.UNAUTHORIZED.value(),
                    UNAUTHORIZED_STATUS,
                    INVALID_EMAIL_OR_PASSWORD,
                    POST_LOGIN_API_PATH
            );
        }

        return user;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return Optional.ofNullable(usersRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.NOT_FOUND.value(),
                NOT_FOUND_STATUS,
                USER_NOT_FOUND,
                GET_USER_API_PATH + id
        )));
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
