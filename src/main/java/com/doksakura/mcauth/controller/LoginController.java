package com.doksakura.mcauth.controller;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam String name) {

        return null;
    }

}
