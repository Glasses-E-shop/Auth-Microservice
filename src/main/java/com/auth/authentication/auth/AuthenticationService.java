package com.auth.authentication.auth;

import com.auth.authentication.config.JwtService;
import com.auth.authentication.user.Role;
import com.auth.authentication.user.User;
import com.auth.authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateTokenNoExtraClaims(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> user = repository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            User aux = user.get();
            if (passwordEncoder.matches(request.getPassword(), aux.getPassword())) {
                var jwtToken = jwtService.generateTokenNoExtraClaims(aux);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }
        }
        return AuthenticationResponse.builder()
                .token("Denied")
                .build();
    }
}
