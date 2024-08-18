package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.repository.WalletRepository;
import com.example.assignment.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository usersRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
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
        Users savedUsers = usersRepository.save(users);

        walletRepository.save(wallet);

        return savedUsers;
    }

    @Override
    public Optional<Users> updateUser(Long id, Users updatedUsers) {
        return usersRepository.findById(id).map(user -> {
            user.setName(updatedUsers.getName());
            user.setEmail(updatedUsers.getEmail());

            if (updatedUsers.getPassword() != null && !updatedUsers.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(updatedUsers.getPassword());
                user.setPassword(encodedPassword);
            }

            return usersRepository.save(user);
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
