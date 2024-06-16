package com.nic.NIC_PROJECT.Repository;

import com.nic.NIC_PROJECT.Model.Archive;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.nic.NIC_PROJECT.Model.CDocument;

import java.util.Optional;

public interface ArchiveRepository extends MongoRepository<CDocument, Long> {
    @Query("{'applicationTransactionId' :  ?0 }")
    Optional<Archive> findByApplicationTransactionId(Long applicationTransactionId);
}

