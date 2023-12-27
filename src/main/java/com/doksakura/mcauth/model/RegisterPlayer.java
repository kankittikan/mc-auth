package com.doksakura.mcauth.model;

import com.doksakura.mcauth.entity.Player;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterPlayer {
    private Player player;
    private String id;
    private LocalDateTime expired;
}