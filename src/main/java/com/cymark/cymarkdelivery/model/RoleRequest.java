package com.cymark.cymarkdelivery.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleRequest {
    private Timestamp updatedAt;
    private String roleName;
    private String message;
    private String email;
}
