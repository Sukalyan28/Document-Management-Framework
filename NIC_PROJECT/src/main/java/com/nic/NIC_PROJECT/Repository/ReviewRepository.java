package com.nic.NIC_PROJECT.Repository;

import com.nic.NIC_PROJECT.Model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findByApplicationTransactionId(long application_id);

}