package com.nic.NIC_PROJECT.Repository;

import com.nic.NIC_PROJECT.Model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByClientId(String username);
}