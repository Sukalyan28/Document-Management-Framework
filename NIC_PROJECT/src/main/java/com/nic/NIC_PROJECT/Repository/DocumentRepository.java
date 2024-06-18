package com.nic.NIC_PROJECT.Repository;


import com.nic.NIC_PROJECT.Model.CDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends MongoRepository<CDocument, UUID> {
    @Query("{ 'created_for.person_id': ?0 }")
    List<CDocument> findByPersonId(int person_id);

    @Query("{'file_information.application_transaction_id' :  ?0}")
    Optional<CDocument> findByApplicationTransactionId(long application_transaction_id);

}
