package com.doksakura.mcauth.controller;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/produce")
    public String produce(@RequestParam String uuid, @RequestParam String name) {
        return clientService.produce(uuid, name);
    }

    @PostMapping("/grant")
    public String auth(@RequestParam String name) {
        if(MapSession.isGranted(name)) return "OK";
        return "Failed";
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String name) {
        clientService.logout(name);
    }
}
