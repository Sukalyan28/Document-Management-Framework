package com.nic.NIC_PROJECT.Model;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Watermark {
    private long applicationTransactionId;
    private String watermark;
}
