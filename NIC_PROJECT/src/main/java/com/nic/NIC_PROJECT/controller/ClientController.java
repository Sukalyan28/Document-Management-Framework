package com.nic.NIC_PROJECT.controller;


import com.nic.NIC_PROJECT.Model.Client;
import com.nic.NIC_PROJECT.Payload.SignUpRequest;
import com.nic.NIC_PROJECT.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/signup")
    public ResponseEntity<Client> signup(@RequestParam String client_id, @RequestBody SignUpRequest signUpRequest) {
        return clientService.signup(client_id, signUpRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam long mobileNo) {
        Optional<String> otp = clientService.login(mobileNo);
        return otp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Invalid mobile number"));
    }

}
