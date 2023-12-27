package com.doksakura.mcauth.service;

import com.doksakura.mcauth.common.IndexRespond;
import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndexService {
    @Autowired
    private PlayerRepository repository;

    public IndexRespond isRegistered(String uuid) {
        Validate validate = MapSession.getValidate(uuid);
        if(validate == null) return IndexRespond.NOT_ALLOW;

        Optional<Player> player = repository.findById(validate.getName());
        if(player.isEmpty()) return IndexRespond.REGISTER;
        return IndexRespond.LOGIN;
    }
}