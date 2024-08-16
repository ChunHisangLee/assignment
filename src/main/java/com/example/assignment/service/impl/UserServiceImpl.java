package com.example.assignment.service.impl;

import com.example.assignment.entity.User;
import com.example.assignment.entity.Wallet;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.repository.WalletRepository;
import com.example.assignment.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public User createUser(User user) {
        // Initialize the user's wallet with 1,000 USD
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);
        wallet.setUser(user);

        user.setWallet(wallet);
        User savedUser = userRepository.save(user);

        // Save the wallet after saving the user to ensure the relationship is established
        walletRepository.save(wallet);

        return savedUser;
    }

    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
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
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
