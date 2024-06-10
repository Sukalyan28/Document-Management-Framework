package com.nic.NIC_PROJECT.Service;



import com.nic.NIC_PROJECT.Payload.RegisterRequest;
import com.nic.NIC_PROJECT.Payload.initResponse;
import com.nic.NIC_PROJECT.Payload.initRequest;
import com.nic.NIC_PROJECT.Model.Role;
import com.nic.NIC_PROJECT.Model.User;
import com.nic.NIC_PROJECT.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.nic.NIC_PROJECT.jwt.jwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public initResponse register(RegisterRequest request){
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user.getUsername());
        return initResponse.builder().token(jwtToken).build();
    }

    public initResponse authenticate(initRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        var jwtToken = jwtService.generateToken(user.getUsername());
        return (initResponse.builder().token(jwtToken).build());
    }
}
