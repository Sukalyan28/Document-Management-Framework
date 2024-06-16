package com.nic.NIC_PROJECT.Model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PdfPasswordRequest {
    // Getters and setters
    private long applicationTransactionId;
    private String password;

    // Constructors (if needed)
    public PdfPasswordRequest() {
    }

    public PdfPasswordRequest(long applicationTransactionId, String password) {
        this.applicationTransactionId = applicationTransactionId;
        this.password = password;
    }

}
