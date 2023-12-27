package com.doksakura.mcauth.controller;

import com.doksakura.mcauth.common.IndexRespond;
import com.doksakura.mcauth.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private IndexService service;

    @GetMapping("/")
    public String index(@RequestParam String uuid) {
        IndexRespond indexRespond = service.isRegistered(uuid);
        if(indexRespond == IndexRespond.REGISTER) return "redirect:/register?uuid=" + uuid;
        if(indexRespond == IndexRespond.LOGIN) return "redirect:/login?uuid=" + uuid;
        return "redirect:/error";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
