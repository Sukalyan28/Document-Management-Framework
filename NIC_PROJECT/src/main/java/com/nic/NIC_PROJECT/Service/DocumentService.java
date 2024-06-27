package com.nic.NIC_PROJECT.Service;

import com.nic.NIC_PROJECT.Model.Archive;
import com.nic.NIC_PROJECT.Model.CDocument;
import com.nic.NIC_PROJECT.Model.PdfPasswordRequest;
import com.nic.NIC_PROJECT.Model.Review;
import com.nic.NIC_PROJECT.Repository.ArchiveRepository;
import com.nic.NIC_PROJECT.Repository.DocumentRepository;
import com.nic.NIC_PROJECT.Repository.ReviewRepository;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    @Autowired
    public ArchiveRepository archiveRepository;

    public UUID saveDocument(CDocument document) {
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

    public ResponseEntity<CDocument> getDocumentById(UUID documentId) {
        System.out.println("searching for document id: " + documentId);
        CDocument CDocument = documentRepository.findById(documentId).orElse(null);

        System.out.println("Document found : " + CDocument);
        return ResponseEntity.ok(CDocument);

    }
    public List<CDocument> getDocumentsByPersonId(int personId) {
        System.out.println("searching for document with personId: " + personId);
        return documentRepository.findByPersonId(personId);
    }

    public Review saveOrUpdateReview(Review review){
        Optional<Review> existingReview = reviewRepository.findByApplicationTransactionId(review.getApplication_transaction_id());

        if(existingReview.isPresent()){
            Review existing = existingReview.get();
            existing.setReview(review.getReview());

            return reviewRepository.save(existing);
        }

        return reviewRepository.save(review);
    }

//    public String deleteDocument(UUID documentId) {
//        Document document = documentRepository.findById(documentId)
//                .orElseThrow(() -> new RuntimeException("Document not found"));
//
//        mongoTemplate.save(document, "archive_documents");
//        LOGGER.info("Document archived successfully with ID: " + documentId);
//
//        documentRepository.deleteById(documentId);
//        LOGGER.info("Document deleted successfully with ID: " + documentId);
//
//        return "Document archived successfully";
//    }
    public Archive archiveDocument(Archive archiveDocument) {

        Optional<Archive> existingArchive = archiveRepository.findByApplicationTransactionId(archiveDocument.getApplication_transaction_id());
        Optional<CDocument> archivedDocument = documentRepository.findByApplicationTransactionId(archiveDocument.getApplication_transaction_id());

        if (existingArchive.isPresent()) {
            Archive archive = existingArchive.get();
            archive.setArchival_comments(archiveDocument.getArchival_comments());
            return archiveRepository.save(archive);
        }

        archivedDocument.ifPresent(document -> documentRepository.deleteById(document.getDocument_id()));

        return archiveRepository.save(archiveDocument);
    }



    public CDocument addWatermarkToDocument(long applicationTransactionId, String watermark) throws IOException {
        Optional<CDocument> existingDocument = documentRepository.findByApplicationTransactionId(applicationTransactionId);

        if (!existingDocument.isPresent()) {
            throw new IOException("Document not found");
        }

       CDocument clientCDocument = existingDocument.get();

        byte[] pdfBytes = Base64.getDecoder().decode(clientCDocument.getDocument().getActual_document_base_64());

        PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));

        //loop to add watermark to each page
        for(PDPage page : document.getPages()){
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 50);
            contentStream.setNonStrokingColor(200, 200, 200);

            float stringWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(watermark) / 1000 * 50;
            float stringHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getCapHeight() / 1000 * 50;

            //calculating middle coordinates
            float centerX = (pageWidth - stringWidth) / 2;
            float centerY = (pageHeight - stringHeight) / 3;
            //Light Grey colour
            contentStream.beginText();
            contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(45), 200, 400));  // adjust the position and angle as required
            contentStream.newLineAtOffset(100,300);
            contentStream.showText(watermark);
            contentStream.endText();
            contentStream.close();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        String base64WatermarkedPdf = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        clientCDocument.getDocument().setActual_document_base_64(base64WatermarkedPdf);

        documentRepository.deleteById(existingDocument.get().getDocument_id());
        documentRepository.save(clientCDocument);

        return clientCDocument;

    }



    public Optional<Review> getReviewByApplicationTransactionId(long applicationTransactionId) {
        return reviewRepository.findByApplicationTransactionId(applicationTransactionId);
    }



    public Optional<Archive> getArchiveDocumentByApplicationTransactionId(long applicationTransactionId) {
        return archiveRepository.findByApplicationTransactionId(applicationTransactionId);
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


    public void deleteDocumentById(UUID documentId) {
        documentRepository.deleteById(documentId);
    }
    public CDocument updateDocument(CDocument document) {
        Date date = new Date();
        document.setCreated_on(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        Date expiryDate = calendar.getTime();
        document.setExpiry_on(expiryDate);

        return documentRepository.save(document);
    }


    public String addPasswordToPdf(PdfPasswordRequest request) throws IOException {
        Optional<CDocument> existingDocument = documentRepository.findByApplicationTransactionId(request.getApplication_transaction_id());

        if (!existingDocument.isPresent()) {
            throw new IOException("Document not found");
        }

        CDocument clientDocument = existingDocument.get();

        byte[] pdfBytes = Base64.getDecoder().decode(clientDocument.getDocument().getActual_document_base_64());

        PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));

        // Set the password protection
        AccessPermission accessPermission = new AccessPermission();
        StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(
                request.getPassword(), request.getPassword(), accessPermission);

        // Customize the protection policy if necessary
        protectionPolicy.setEncryptionKeyLength(128);  // 128-bit key length
        protectionPolicy.setPermissions(accessPermission);
        document.protect(protectionPolicy);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        String base64PdfWithPassword = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        // Update the ClientDocument with the new Base64 content
        clientDocument.getDocument().setActual_document_base_64(base64PdfWithPassword);

        // Save the updated ClientDocument
        documentRepository.save(clientDocument);

        return base64PdfWithPassword;
    }

}