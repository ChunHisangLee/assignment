package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                    "Conflict",
                    "Email already registered. Try to log in or register with another email.",
                    "POST /api/users"
            );
        }

        // Encode the user's password
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        // Initialize and link the wallet to the user
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);
        wallet.setUsers(users);

        users.setWallet(wallet);

        // Save the user and cascade save the wallet
        return usersRepository.save(users);
    }

    @Override
    public Optional<Users> updateUser(Long id, Users users) {
        Users existingUser = usersRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "User not found.",
                "PUT /api/users/" + id
        ));

        if (usersRepository.findByEmail(users.getEmail()).filter(user -> !user.getId().equals(id)).isPresent()) {
            throw new CustomErrorException(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    "Email already registered by another user.",
                    "PUT /api/users/" + id
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
                "Not Found",
                "User not found.",
                "DELETE /api/users/" + id
        ));
        usersRepository.delete(user);
    }

    @Override
    public Users login(String email, String password) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new CustomErrorException(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Invalid email or password.",
                "POST /api/users/login"
        ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomErrorException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    "Invalid email or password.",
                    "POST /api/users/login"
            );
        }

        return user;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return Optional.ofNullable(usersRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "User not found.",
                "GET /api/users/" + id
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
