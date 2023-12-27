package com.doksakura.mcauth.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reset {
    private String resetId;
    private String uuid;
    private LocalDateTime expired;
}