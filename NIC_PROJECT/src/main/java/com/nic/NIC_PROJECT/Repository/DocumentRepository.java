package com.nic.NIC_PROJECT.Repository;


import com.nic.NIC_PROJECT.Model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DocumentRepository extends MongoRepository<Document, UUID> {

}
