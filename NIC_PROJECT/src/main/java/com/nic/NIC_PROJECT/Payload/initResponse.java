package com.nic.NIC_PROJECT.Payload;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initResponse {
    //private String username;
    //private String email;
    private String token;
    private String message;

}
