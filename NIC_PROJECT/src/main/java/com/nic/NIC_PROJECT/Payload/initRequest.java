package com.nic.NIC_PROJECT.Payload;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initRequest {
    private String username;
//    private String email;
    private String password;
}
