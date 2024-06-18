package com.nic.NIC_PROJECT.Model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PdfPasswordRequest {
    // Getters and setters
    private long application_transaction_id;
    private String password;

    // Constructors (if needed)
    public PdfPasswordRequest() {
    }

    public PdfPasswordRequest(long application_transaction_id, String password) {
        this.application_transaction_id = application_transaction_id;
        this.password = password;
    }

}
