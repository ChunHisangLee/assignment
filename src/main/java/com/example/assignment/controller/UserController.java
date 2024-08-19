package com.example.assignment.controller;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.Users;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.mapper.UsersMapper;
import com.example.assignment.security.JwtAuthenticationResponse;
import com.example.assignment.security.JwtTokenProvider;
import com.example.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import static com.example.assignment.constants.ErrorMessages.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UsersMapper usersMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, UsersMapper usersMapper, JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.usersMapper = usersMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDTO> registerUser(@Valid @RequestBody UsersDTO usersDTO) {
        Users users = usersMapper.convertToEntity(usersDTO);
        users.setPassword(usersDTO.getPassword()); // Set password separately
        Users createdUser = userService.registerUser(users);
        return ResponseEntity.ok(usersMapper.convertToDto(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UsersDTO usersDTO) {
        Users users = usersMapper.convertToEntity(usersDTO);
        users.setPassword(usersDTO.getPassword()); // Set password separately
        Users updatedUser = userService.updateUser(id, users)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND.value(),
                        NOT_FOUND_STATUS,
                        USER_NOT_FOUND,
                        GET_USER_API_PATH + id
                ));
        return ResponseEntity.ok(usersMapper.convertToDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        Users user = userService.getUserById(id)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND.value(),
                        NOT_FOUND_STATUS,
                        USER_NOT_FOUND,
                        GET_USER_API_PATH + id
                ));
        return ResponseEntity.ok(usersMapper.convertToDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDTO loginRequest) {
        // Authenticate the user using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok().build();
    }
}
