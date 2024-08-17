package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.repository.WalletRepository;
import com.example.assignment.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users createUser(Users users) {
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);

        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);
        wallet.setUsers(users);

        users.setWallet(wallet);
        Users savedUsers = userRepository.save(users);

        walletRepository.save(wallet);

        return savedUsers;
    }

    @Override
    public Optional<Users> updateUser(Long id, Users updatedUsers) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUsers.getName());
            user.setEmail(updatedUsers.getEmail());

            if (updatedUsers.getPassword() != null && !updatedUsers.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(updatedUsers.getPassword());
                user.setPassword(encodedPassword);
            }

            return userRepository.save(user);
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
