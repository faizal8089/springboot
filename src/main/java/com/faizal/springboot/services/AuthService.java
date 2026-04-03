package com.faizal.springboot.services;


import com.faizal.springboot.dto.AuthRequestDTO;
import com.faizal.springboot.dto.AuthResponseDTO;
import com.faizal.springboot.models.User;
import com.faizal.springboot.repository.UserRepository;
import com.faizal.springboot.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthService implements UserDetailsService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
    }

    public String register(AuthRequestDTO authRequestDTO){
        User user = new User();
        user.setName(authRequestDTO.name());
        user.setEmail(authRequestDTO.email());
        user.setPassword(passwordEncoder.encode(authRequestDTO.password()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) throws UsernameNotFoundException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.email(), authRequestDTO.password()));
        User user = (User) loadUserByUsername(authRequestDTO.email());
        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token);
    }
}

/**
 * AuthService — handles user registration and login.
 * implements UserDetailsService so Spring Security knows how to load users from DB.
 *
 * DEPENDENCIES:
 * UserRepository      → find and save users in DB
 * JwtUtil             → generate JWT token after successful login
 * PasswordEncoder     → hash passwords before saving, verify during login
 * AuthenticationManager → verifies email + password during login
 *                         both PasswordEncoder and AuthenticationManager are
 *                         defined as @Bean in SecurityConfig
 *
 * CONTROL FLOW:
 *
 * REGISTER:
 * client sends name + email + password
 *      ↓
 * register() creates User object
 *      ↓
 * password hashed via passwordEncoder.encode() — never stored as plain text
 *      ↓
 * role hardcoded as ROLE_USER — client never decides their own role
 *      ↓
 * saved to DB via userRepository.save()
 *      ↓
 * returns "User registered successfully"
 *
 * LOGIN:
 * client sends email + password
 *      ↓
 * authenticationManager.authenticate() verifies credentials
 *      → internally calls loadUserByUsername() to find user
 *      → uses PasswordEncoder to check plain password vs hash in DB
 *      → wrong credentials → throws BadCredentialsException → 401 automatic
 *      → correct credentials → execution continues
 *      ↓
 * load User from DB using email
 *      ↓
 * jwtUtil.generateToken(user) creates signed JWT string
 *      ↓
 * returns AuthResponseDTO(token) to client
 *
 * LOAD USER:
 * loadUserByUsername() is called automatically by Spring Security
 * during authentication — you never call it directly except in login()
 * returns UserDetails (your User entity implements UserDetails)
 * throws UsernameNotFoundException if email not found → Spring returns 401
 */
