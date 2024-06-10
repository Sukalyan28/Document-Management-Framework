package com.nic.NIC_PROJECT.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documents")
public class DocumentModel {



    @Id
    private UUID document_id;

    @Getter
    @Setter
    private String client_id;

    @Getter
    @Setter
    private String client_secret;

    @Getter
    @Setter
    private Date created_on;

    @Getter
    @Setter
    private Date expiry_on;

    @Getter
    @Setter
    private String state_code;

    @Getter
    @Setter
    private String state_name;

    @Getter
    @Setter
    private String department_code;

    @Getter
    @Setter
    private String department_name;

    @Getter
    @Setter
    private String govt;

    @Getter
    @Setter
    private String nodal_officer_name;

    @Getter
    @Setter
    private String getNodal_officer_mobile;

    @Getter
    @Setter
    private String nodal_officer_email;

    @Getter
    @Setter
    private String nodal_officer_designation;

    @Getter
    @Setter
    private String content;


}
