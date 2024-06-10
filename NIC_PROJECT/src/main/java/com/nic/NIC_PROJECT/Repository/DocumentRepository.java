package com.nic.NIC_PROJECT.Repository;


import com.nic.NIC_PROJECT.Model.DocumentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DocumentRepository extends MongoRepository<DocumentModel, UUID> {

}
