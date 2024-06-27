package com.nic.NIC_PROJECT.Service;



import com.nic.NIC_PROJECT.Payload.initRRequest;
import com.nic.NIC_PROJECT.Payload.initResponse;
import com.nic.NIC_PROJECT.Payload.initLRequest;
import com.nic.NIC_PROJECT.Model.Role;
import com.nic.NIC_PROJECT.Model.Client;
import com.nic.NIC_PROJECT.Repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.nic.NIC_PROJECT.jwt.jwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public initResponse register(initRRequest request){
        Date date = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.YEAR, 1);
        Date expiryDate = calender.getTime();

        var client = Client.builder()
                .client_id(request.getClient_id())
                .client_secret(passwordEncoder.encode(request.getClient_secret()))
                .created_on(date)
                .expiry_on(expiryDate)
                .role(Role.CLIENT)
                .build();
        clientRepository.save(client);
        var jwtToken = jwtService.generateToken(client);
        return initResponse.builder().token(jwtToken).build();
    }

    public initResponse authenticate(initLRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getClient_id(), request.getClient_secret())
        );
        var client = clientRepository.findByClientId(request.getClient_id()).orElseThrow();

        var jwtToken = jwtService.generateToken(client);
        return (initResponse.builder().token(jwtToken).build());
    }
}
