package com.doksakura.mcauth.model;

import lombok.Data;

@Data
public class AuthDTO {
    private String id;
    private String name;
    private String email;
    private String password;
}