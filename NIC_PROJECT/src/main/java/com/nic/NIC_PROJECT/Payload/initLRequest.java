package com.nic.NIC_PROJECT.Payload;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initLRequest {
    private String client_id;
    private String client_secret;
}
