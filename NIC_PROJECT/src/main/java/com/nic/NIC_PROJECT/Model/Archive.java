package com.nic.NIC_PROJECT.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "archive_documents")
public class Archive {
    private long applicationTransactionId;
    private String archival_comments;
}
