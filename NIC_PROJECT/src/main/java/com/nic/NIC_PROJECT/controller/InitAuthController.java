package com.nic.NIC_PROJECT.controller;
import  java.lang.*;

import com.nic.NIC_PROJECT.Payload.RegisterRequest;
import com.nic.NIC_PROJECT.Service.AuthenticationService;
import com.nic.NIC_PROJECT.Repository.UserRepository;

import com.nic.NIC_PROJECT.Payload.initResponse;
import com.nic.NIC_PROJECT.Payload.initRequest;
import com.nic.NIC_PROJECT.jwt.jwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InitAuthController {

    private final AuthenticationService service;

//    @PostMapping("/register")
//    public ResponseEntity<initResponse> register(@RequestBody initRequest request) {
//        return ResponseEntity.ok(service.register(request));
//    }


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtService jwtUtil;

    @PostMapping("/init")
    public ResponseEntity<initResponse> init(@RequestBody initRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            try {
                initResponse response = service.authenticate(request);
                response.setMessage("Logged in successfully");
                System.out.println("Logged in successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(request.getUsername());
            registerRequest.setPassword(request.getPassword());

            try {
                initResponse response = service.register(registerRequest);
                response.setMessage("New User created and logged in");
                System.out.println("New User created and logged in: ");
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
}