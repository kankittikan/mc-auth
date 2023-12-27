package com.doksakura.mcauth.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Validate {
    private String uuid;
    private String name;
    private LocalDateTime expired;
}
