package com.doksakura.mcauth.common;

import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.model.RegisterPlayer;
import com.doksakura.mcauth.model.Reset;
import com.doksakura.mcauth.model.Validate;

import java.time.LocalDateTime;
import java.util.*;

public class MapSession {
    public static Map<String, Validate> validate = new HashMap<>();
    public static Map<String, Player> granted = new HashMap<>();
    public static List<Reset> resetList = new ArrayList<>();

    public static Map<String, RegisterPlayer> verify = new HashMap<>();

    public static Validate getValidate(String uuid) {
        resetList.removeIf(r -> r.getExpired().isBefore(LocalDateTime.now()));
        for (RegisterPlayer p : verify.values()) {
            if (p.getExpired().isBefore(LocalDateTime.now())) verify.remove(p.getPlayer().getName());
        }
        for (Validate s : validate.values()) {
            if (s.getUuid().equals(uuid)) {
                if (s.getExpired().isBefore(LocalDateTime.now())) {
                    validate.remove(s.getName());
                    continue;
                }
                return s;
            }
        }
        return null;
    }

    public static boolean isGranted(String name) {
        return granted.containsKey(name);
    }

    public static boolean checkInGame(String uuid) {
        Validate validate = getValidate(uuid);
        return validate != null;
    }

    public static boolean checkInVerify(String uuid) {
        Validate validate = getValidate(uuid);
        if(validate == null) return false;
        return verify.containsKey(validate.getName());
    }
}