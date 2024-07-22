package com.relevans.service;

import com.relevans.dto.LoginDto;
import com.relevans.dto.UserDto;
import com.relevans.repo.IUserRepo;
import com.relevans.repo.model.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final IUserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtServices;

    @Transactional
    public UserDto save(UserDto userDto) {
        LOGGER.log(Level.INFO, "Saving user: {0}", userDto);
        UserModel userModel = modelMapper.map(userDto, UserModel.class);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword())); // Encriptar contraseña
        userModel = userRepo.save(userModel);
        UserDto savedUserDto = modelMapper.map(userModel, UserDto.class);
        LOGGER.log(Level.INFO, "User saved: {0}", savedUserDto);
        return savedUserDto;
    }

    public String login(LoginDto loginDto) {
        LOGGER.log(Level.INFO, "Authenticating user: {0}", loginDto.getUsername());
        Optional<UserModel> userOpt = userRepo.findByUsername(loginDto.getUsername());
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return jwtServices.getToken(loadUserByUsername(user.getUsername()));
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
