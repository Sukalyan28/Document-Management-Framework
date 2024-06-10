package com.nic.NIC_PROJECT.Repository;

import com.nic.NIC_PROJECT.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
