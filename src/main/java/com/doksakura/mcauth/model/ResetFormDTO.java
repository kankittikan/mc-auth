package com.doksakura.mcauth.model;

import lombok.Data;

@Data
public class ResetFormDTO {
    private String resetId;
    private String password;
    private String confirmPassword;
}
