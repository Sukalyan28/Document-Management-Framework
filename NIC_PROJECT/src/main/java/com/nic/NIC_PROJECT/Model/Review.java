package com.nic.NIC_PROJECT.Model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private long application_transaction_id;
    private String review;

}
