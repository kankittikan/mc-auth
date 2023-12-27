package com.doksakura.mcauth.service;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.model.AuthDTO;
import com.doksakura.mcauth.model.PlayerDTO;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private PlayerRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Player getPlayer(String uuid) {
        Validate validate = MapSession.getValidate(uuid);
        if(validate == null) return null;

        Optional<Player> player = repository.findById(validate.getName());
        return player.orElse(null);
    }

    public boolean isAuth(AuthDTO authDTO) throws AttributeNotFoundException {
        Optional<Player> player = repository.findById(authDTO.getName());
        if(player.isEmpty()) throw new AttributeNotFoundException();

        if(passwordEncoder.matches(authDTO.getPassword(), player.get().getPassword())) {
            player.get().setLastLogin(LocalDateTime.now());
            repository.save(player.get());
            MapSession.validate.remove(authDTO.getName());
            MapSession.resetList.removeIf(reset -> reset.getUuid().equals(authDTO.getId()));
            MapSession.granted.put(authDTO.getName(), player.get());
            return true;
        }
        return false;
    }
}