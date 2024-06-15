package com.nic.NIC_PROJECT.Repository;


import com.nic.NIC_PROJECT.Model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends MongoRepository<Document, UUID> {
    @Query("{ 'created_for.person_id': ?0 }")
    List<Document> findByPersonId(int person_id);
}
