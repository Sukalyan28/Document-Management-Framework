package com.nic.NIC_PROJECT.controller;

import com.nic.NIC_PROJECT.Model.Archive;
import com.nic.NIC_PROJECT.Model.CDocument;
import com.nic.NIC_PROJECT.Model.Review;
import com.nic.NIC_PROJECT.Repository.DocumentRepository;
import com.nic.NIC_PROJECT.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nic.NIC_PROJECT.Model.Watermark;
import com.nic.NIC_PROJECT.Model.PdfPasswordRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    public DocumentController(DocumentService documentService, DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
    }


    @PostMapping("/savedocument")
    public String saveDocument(@RequestBody CDocument CDocument) {
        UUID document_id = documentService.saveDocument(CDocument);
        return "Document saved successfully with id: " + document_id;
    }


    @GetMapping("/getadocument/{id}")
    public ResponseEntity<CDocument> getDocument(@PathVariable("id") UUID document_id){
        System.out.println("received request for document ID: " + document_id);
        ResponseEntity<CDocument> document = documentService.getDocumentById(document_id);

        return ResponseEntity.ok(document.getBody());
    }

    @GetMapping("/documentofaperson/{person_id}")
    public ResponseEntity<?> getDocumentByPersonId(@PathVariable int person_id) {
        System.out.println("Received request for document with person ID: " + person_id);
        List<CDocument> CDocuments = documentService.getDocumentsByPersonId(person_id);

        if (CDocuments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(CDocuments);
        }
    }
    @PostMapping("/reviewdocument")
    public  ResponseEntity<Review> saveOrUpdateReview(@RequestBody Review review){
        Optional<CDocument> documentOptional = documentRepository.findByApplicationTransactionId(review.getApplication_transaction_id());

        if(documentOptional.isPresent()){
            review.setApplication_transaction_id(documentOptional.get().getFile_information().getApplication_transaction_id());
            Review savedReview = documentService.saveOrUpdateReview(review);

            return ResponseEntity.ok(savedReview);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/archivedocument")
    public ResponseEntity<?> archiveDocument(@RequestBody Archive archiveDocument) {
        Optional<CDocument> clientDocumentOptional = documentRepository.findByApplicationTransactionId(archiveDocument.getApplication_transaction_id());

        if (clientDocumentOptional.isPresent()) {
            Archive savedArchiveDocument = documentService.archiveDocument(archiveDocument);
            return ResponseEntity.ok(savedArchiveDocument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/editdocumentinfo/{id}")
//    public ResponseEntity<Document> updateDocument(@PathVariable("id") UUID documentId, @RequestBody Document updatedDocument) {
//        System.out.println("Received request to update document ID: " + documentId);
//        Document document = documentService.updateDocument(documentId, updatedDocument);
//        return ResponseEntity.ok(document);
//    }

    @PostMapping("/addwatermarktodocument")
    public ResponseEntity<?> addWatermarkToDocument(@RequestBody Watermark watermarkRequest){
        try {
            CDocument updatedCDocument = documentService.addWatermarkToDocument(watermarkRequest.getApplication_transaction_id(),
                    watermarkRequest.getWatermark());
            return new ResponseEntity<>(updatedCDocument, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/viewreviewlog/{application_transaction_id}")
    public ResponseEntity<Review> getReviewByApplicationId(@PathVariable long application_transaction_id) {
        Optional<Review> reviewOptional = documentService.getReviewByApplicationTransactionId(application_transaction_id);

        return reviewOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vieweditlog/{applicationTransactionId}")
    public ResponseEntity<Archive> getArchiveDocumentByApplicationTransactionId(@PathVariable long applicationTransactionId) {
        Optional<Archive> archiveDocumentOptional = documentService.getArchiveDocumentByApplicationTransactionId(applicationTransactionId);

        if (archiveDocumentOptional.isPresent()) {
            return ResponseEntity.ok(archiveDocumentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/editdocumentinfo/{documentId}")
    public ResponseEntity<?> editDocumentInfo(@PathVariable UUID documentId, @RequestBody CDocument newDocument) {
        ResponseEntity<CDocument> responseEntity = documentService.getDocumentById(documentId);

        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            documentService.deleteDocumentById(documentId);

            newDocument.setDocument_id(documentId);
            CDocument savedDocument = documentService.updateDocument(newDocument);

            return ResponseEntity.ok(savedDocument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/setadocumentconfidential")
    public ResponseEntity<?> addPasswordToPdf(@RequestBody PdfPasswordRequest request) {
        try {
            String base64PdfWithPassword = documentService.addPasswordToPdf(request);
            return ResponseEntity.ok(base64PdfWithPassword);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to add password to PDF: " + e.getMessage());
        }
    }

}
