package com.doksakura.mcauth.controller;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.model.AuthDTO;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.AttributeNotFoundException;

@Controller
public class LoginController {
    @Autowired
    private LoginService service;

    @GetMapping("/login")
    public String login(Model model, @RequestParam String uuid) {
        if(!MapSession.checkInGame(uuid)) return "redirect:/error";

        Player player = service.getPlayer(uuid);
        if(player == null) return "redirect:/error";

        model.addAttribute("name", player.getName());
        model.addAttribute("email", player.getEmail());
        model.addAttribute("uuid", uuid);

        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute AuthDTO authDTO) {
        if(!MapSession.checkInGame(authDTO.getId())) return "redirect:/error";

        try {
            if(!service.isAuth(authDTO)) {
                Player player = service.getPlayer(authDTO.getId());
                if(player == null) return "redirect:/error";
                model.addAttribute("name", player.getName());
                model.addAttribute("email", player.getEmail());
                model.addAttribute("uuid", authDTO.getId());
                model.addAttribute("error", true);
                return "login";
            }
        } catch (AttributeNotFoundException e) {
            return "redirect:/error";
        }
        model.addAttribute("name", authDTO.getName());
        return "success";
    }
}