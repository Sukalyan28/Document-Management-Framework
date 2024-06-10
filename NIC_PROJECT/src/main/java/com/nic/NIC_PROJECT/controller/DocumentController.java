package com.nic.NIC_PROJECT.controller;

import com.nic.NIC_PROJECT.Model.Document;
import com.nic.NIC_PROJECT.Model.Review;
import com.nic.NIC_PROJECT.Repository.DocumentRepository;
import com.nic.NIC_PROJECT.Repository.ReviewRepository;
import com.nic.NIC_PROJECT.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;


    @PostMapping("/savedocument")
    public String saveDocument(@RequestBody Document document) {
        UUID document_id = documentService.saveDocument(document);
        return "Document saved successfully with id: " + document_id;
    }


    @GetMapping("/getadocument/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable("id") UUID document_id){
        System.out.println("received request for document ID: " + document_id);
        ResponseEntity<Document> document = documentService.getDocumentById(document_id);

        return ResponseEntity.ok(document.getBody());
    }

    @GetMapping("/documentofaperson/{clientId}")
    public ResponseEntity<?> getDocumentByClientId(@PathVariable String clientId) {
        System.out.println("Received request for document with clientId: " + clientId);
        return documentService.getDocumentByClientId(clientId);
    }

    @PostMapping("/reviewdocument")
    public  ResponseEntity<Review> saveOrUpdateReview(@RequestBody Review review){
        Optional<Document> documentOptional = documentRepository.findById(UUID.fromString(review.getDocumentId()));

        if(documentOptional.isPresent()){
            review.setDocumentId(String.valueOf(documentOptional.get().getDocument_id()));
            Review savedReview = documentService.saveOrUpdateReview(review);
            System.out.println("Reviewed successfully");
            return ResponseEntity.ok(savedReview);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


}
