package com.nic.NIC_PROJECT.Service;

import com.nic.NIC_PROJECT.Model.Document;
import com.nic.NIC_PROJECT.Model.Review;
import com.nic.NIC_PROJECT.Repository.DocumentRepository;
import com.nic.NIC_PROJECT.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;


import java.util.*;
import java.util.logging.Logger;

@Service
public class DocumentService {
    @Autowired
    private final DocumentRepository documentRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    private ReviewRepository reviewRepository;
    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());
    @Autowired
    public DocumentService(DocumentRepository documentRepository, MongoTemplate mongoTemplate) {
        this.documentRepository = documentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public UUID saveDocument(Document document) {
        document.setDocument_id(UUID.randomUUID());

        Date date = new Date();
        document.setCreated_on(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        Date expiryDate = calendar.getTime();
        document.setExpiry_on(expiryDate);

        documentRepository.save(document);
        return document.getDocument_id();
    }

    public ResponseEntity<Document> getDocumentById(UUID documentId) {
        System.out.println("searching for document id: " + documentId);
        Document document = documentRepository.findById(documentId).orElse(null);

        System.out.println("Document found : " + document);
        return ResponseEntity.ok(document);

    }
    public List<Document> getDocumentsByPersonId(int personId) {
        return documentRepository.findByPersonId(personId);
    }

    public Review saveOrUpdateReview(Review review){
        Optional<Review> existingReview = reviewRepository.findByDocumentId(review.getDocumentId());

        if(existingReview.isPresent()){
            Review existing = existingReview.get();
            existing.setFeedback(review.getFeedback());

            return reviewRepository.save(existing);
        }

        return reviewRepository.save(review);
    }

    public String deleteDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        mongoTemplate.save(document, "archive_documents");
        LOGGER.info("Document archived successfully with ID: " + documentId);

        documentRepository.deleteById(documentId);
        LOGGER.info("Document deleted successfully with ID: " + documentId);

        return "Document archived successfully";
    }


//    @Transactional
//    public Document updateDocument(UUID documentId, Document updatedDocument) {
//        System.out.println("Updating document ID: " + documentId);
//
//        Document documentToUpdate = documentRepository.findById(documentId)
//                .orElseThrow(() -> new RuntimeException("Document not found with id " + documentId));
//
//        if (updatedDocument.getClient_id() != null) {
//            documentToUpdate.setClient_id(updatedDocument.getClient_id());
//        }
//        if (updatedDocument.getClient_secret() != null) {
//            documentToUpdate.setClient_secret(updatedDocument.getClient_secret());
//        }
//        if (updatedDocument.getState_code() != null) {
//            documentToUpdate.setState_code(updatedDocument.getState_code());
//        }
//        if (updatedDocument.getState_name() != null) {
//            documentToUpdate.setState_name(updatedDocument.getState_name());
//        }
//        if (updatedDocument.getDepartment_code() != null) {
//            documentToUpdate.setDepartment_code(updatedDocument.getDepartment_code());
//        }
//        if (updatedDocument.getDepartment_name() != null) {
//            documentToUpdate.setDepartment_name(updatedDocument.getDepartment_name());
//        }
//        if (updatedDocument.getGovt() != null) {
//            documentToUpdate.setGovt(updatedDocument.getGovt());
//        }
//        if (updatedDocument.getNodal_officer_name() != null) {
//            documentToUpdate.setNodal_officer_name(updatedDocument.getNodal_officer_name());
//        }
//        if (updatedDocument.getNodal_officer_mobile() != null) {
//            documentToUpdate.setNodal_officer_mobile(updatedDocument.getNodal_officer_mobile());
//        }
//        if (updatedDocument.getNodal_officer_email() != null) {
//            documentToUpdate.setNodal_officer_email(updatedDocument.getNodal_officer_email());
//        }
//        if (updatedDocument.getNodal_officer_designation() != null) {
//            documentToUpdate.setNodal_officer_designation(updatedDocument.getNodal_officer_designation());
//        }
//
//        // Save the updated document
//        Document savedDocument = documentRepository.save(documentToUpdate);
//        System.out.println("Document updated: " + savedDocument);
//
//        return savedDocument;
//    }



}