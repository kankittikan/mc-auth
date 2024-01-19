package com.doksakura.mcauth.service;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.model.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService {

    public String produce(String uuid, String name) {
        if(MapSession.isGranted(name)) return "Already";

        Validate validate = new Validate();
        validate.setUuid(uuid);
        validate.setName(name);
        validate.setExpired(LocalDateTime.now().plusMinutes(15));

        MapSession.validate.put(name, validate);
        return "OK";
    }

    public void logout(String name) {
        MapSession.granted.remove(name);
        MapSession.validate.remove(name);
        MapSession.verify.remove(name);
    }
}