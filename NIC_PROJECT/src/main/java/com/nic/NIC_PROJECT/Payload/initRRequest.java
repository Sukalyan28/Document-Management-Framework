package com.nic.NIC_PROJECT.Payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initRRequest {
    private String client_id;
    private String client_secret;
    private Date created_on;
    private Date expiry_on;
}
