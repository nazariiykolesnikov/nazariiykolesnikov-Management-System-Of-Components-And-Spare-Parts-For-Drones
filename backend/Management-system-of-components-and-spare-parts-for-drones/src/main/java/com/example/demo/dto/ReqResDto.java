package com.example.demo.dto;

import com.example.demo.entity.OurEngineers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResDto {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String address;
    private String role;
    private String email;
    private String password;
    private String phoneNumber;
    private OurEngineers ourEngineers;
    private List<OurEngineers> ourEngineersList;

}
