package com.nic.NIC_PROJECT.Service;

import com.nic.NIC_PROJECT.Model.Client;
import com.nic.NIC_PROJECT.Config.DateConfig;
import com.nic.NIC_PROJECT.Payload.SignUpRequest;
import com.nic.NIC_PROJECT.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Optional<String> login(long mobileNo) {
        Optional<Client> optionalClient = clientRepository.findByMobileNo(mobileNo);
        return optionalClient.isPresent() ? Optional.of(generateOtp()) : Optional.empty();
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    public Optional<Client> signup(String client_Id, SignUpRequest signUpRequest) {
        if (!DateConfig.isValidDate(signUpRequest.getDob())) {
            throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy");
        }

        return clientRepository.findByClientId(client_Id)
                .map(client -> {
                    client.setMobileNo(signUpRequest.getMobileNo());
                    client.setEmailId(signUpRequest.getEmailId());
                    client.setName(signUpRequest.getName());
                    client.setGender(signUpRequest.getGender());
                    client.setDob(signUpRequest.getDob());
                    client.setAddress(signUpRequest.getAddress());
                    return clientRepository.save(client);
                });

    }
}
