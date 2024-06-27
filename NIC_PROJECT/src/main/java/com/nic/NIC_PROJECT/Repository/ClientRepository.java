package com.nic.NIC_PROJECT.Repository;

import com.nic.NIC_PROJECT.Model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByMobileNo(long mobileNo);
    @Query("{'client_id': ?0}")
    Optional<Client> findByClientId(String client_id);
}