package com.cymark.cymarkdelivery.dtos.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Builder
@Data
public class SwitchRoleResponse {

    private Timestamp timestamp;
    private String message;
    private HttpStatus status;
    private int statusCode;


}
