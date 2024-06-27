package com.nic.NIC_PROJECT.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.annotation.Id;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="client")
public class Client implements UserDetails {

    @Id
    private String client_id;
    private String client_secret;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date created_on;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    private Date expiry_on;

    @Field("mobile_no")
    private long mobileNo;

    @Field("email_id")
    private String emailId;

    private String name;
    private String gender;
    private String dob;
    private String address;




    @Setter
    @Getter
    private Role role;


    @JsonIgnore
    public String getUsername() {
        return client_id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.client_id = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    public String getPassword() {
        return client_secret;

    }

    public void setPassword(String password) {
        this.client_secret = password;
    }

}
