package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Users createUser(Users users) {
        // Encode the user's password
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);

        // Initialize and link the wallet to the user
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);
        wallet.setUsers(users);

        users.setWallet(wallet);

        // Save the user and cascade save the wallet
        return usersRepository.save(users);
    }

    @Override
    public Optional<Users> updateUser(Long id, Users updatedUsers) {
        return usersRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUsers.getName());
            existingUser.setEmail(updatedUsers.getEmail());

            if (StringUtils.hasText(updatedUsers.getPassword())) {
                String encodedPassword = passwordEncoder.encode(updatedUsers.getPassword());
                existingUser.setPassword(encodedPassword);
            }

            return usersRepository.save(existingUser);
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
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
