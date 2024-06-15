package com.nic.NIC_PROJECT.controller;
import  java.lang.*;

import com.nic.NIC_PROJECT.Payload.initRRequest;
import com.nic.NIC_PROJECT.Service.AuthenticationService;
import com.nic.NIC_PROJECT.Repository.ClientRepository;

import com.nic.NIC_PROJECT.Payload.initResponse;
import com.nic.NIC_PROJECT.Payload.initLRequest;
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
    private final ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtService jwtUtil;

    //getters and setters of register request class is used here. UserDetails package not used


    @PostMapping("/init")
    public ResponseEntity<initResponse> init(@RequestBody initLRequest request) {
        if (clientRepository.findByClientId(request.getClient_id()).isPresent()) {
            try {
                initResponse response = service.authenticate(request);
                response.setMessage("Client Logged in successfully");
                System.out.println("Client Logged in successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            initRRequest initRRequest = new initRRequest();
            initRRequest.setClient_id(request.getClient_id());
            initRRequest.setClient_secret(request.getClient_secret());

            try {
                initResponse response = service.register(initRRequest);
                response.setMessage("New Client created and logged in");
                System.out.println("New Client created and logged in: ");
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
}