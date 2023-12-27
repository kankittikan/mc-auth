package com.doksakura.mcauth.model;

import lombok.Data;

@Data
public class RegisterDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}