package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
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
    public Users createUser(Users users) {
        // Initialize the users's wallet with 1,000 USD
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);
        wallet.setUsers(users);

        users.setWallet(wallet);
        Users savedUsers = userRepository.save(users);

        // Save the wallet after saving the users to ensure the relationship is established
        walletRepository.save(wallet);

        return savedUsers;
    }

    @Override
    public Optional<Users> updateUser(Long id, Users updatedUsers) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUsers.getName());
            user.setEmail(updatedUsers.getEmail());
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
}
