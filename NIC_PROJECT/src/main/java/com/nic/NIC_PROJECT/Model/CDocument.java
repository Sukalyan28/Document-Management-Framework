package com.nic.NIC_PROJECT.Model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
public class CDocument {



    @Id
    private UUID document_id;
    private Date created_on;
    private Date expiry_on;
    private Application application;
    private Module module;
    private Workflow workflow;
    private FileInformation file_information;
    private CreatedBy created_by;
    private CreatedFor created_for;
    private DocumentContent document;
    private AdditionalInfo additional_info_1;
    private AdditionalInfo additional_info_2;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Application {
        private String application_id;
        private String application_name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Module {
        private String module_id;
        private String module_name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Workflow {
        private String workflow_id;
        private String workflow_name;
    }

    @Data
    @Builder
    //@NoArgsConstructor
    @AllArgsConstructor
    public static class FileInformation {
        private long application_transaction_id;
        private String file_type;
        private String file_name;
        private Date creation_date; // consider changing to Date if needed
        private Date creation_time; // consider changing to Date if needed
        private String system_ip;
        private String system_mac;

        // Constructor to initialize creation_date and creation_time with system time
        public FileInformation() {
            this.creation_date = new Date();
            this.creation_time = new Date();
            this.system_ip = getSystemIpAddress();
            this.system_mac = getSystemMacAddress();
        }

        // Method to get system's IP address
        private String getSystemIpAddress() {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                return inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                // Handle or log the exception
                return null;
            }
        }

        // Method to get system's MAC address
        private String getSystemMacAddress() {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                    }
                    return sb.toString();
                }
            } catch (SocketException | UnknownHostException e) {
                // Handle or log the exception
            }
            return null;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatedBy {
        private String employee_code;
        private String employee_name;
        private String designation;
        private String organization;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatedFor {
        private int person_id;
        private String name;
        private String gender;
        private int age_yr;
        private long mobile_number;
        private long additional_id_1;
        private String additional_id_1_description;
        private String additional_id_2;
        private String additional_id_2_description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentContent {
        private String actual_document_base_64;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalInfo {
        private Info info_1;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Info {
            private String greeting;
        }
    }


}
