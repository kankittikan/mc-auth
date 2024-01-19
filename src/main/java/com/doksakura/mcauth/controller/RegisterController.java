package com.doksakura.mcauth.controller;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.exception.StringFormatException;
import com.doksakura.mcauth.model.RegisterDTO;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.service.LoginService;
import com.doksakura.mcauth.service.RegisterService;
import com.doksakura.mcauth.service.ResetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.AttributeNotFoundException;
import java.util.UUID;

@Controller
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public String register(Model model, @RequestParam String uuid) {
        if(!MapSession.checkInGame(uuid) || registerService.isRegistered(uuid)) return "error";

        Validate validate = MapSession.getValidate(uuid);
        if(validate == null) return "redirect:/error";

        model.addAttribute("name", validate.getName());
        model.addAttribute("uuid", uuid);
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute RegisterDTO registerDTO) {
        String email;
        try {
            try {
                email = registerService.register(registerDTO);
            } catch (JsonProcessingException e) {
                return "redirect:/error";
            }
        } catch (StringFormatException e) {
            model.addAttribute("name", registerDTO.getName());
            model.addAttribute("uuid", registerDTO.getId());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        model.addAttribute("email", email);
        return "verifyConfirm";
    }

    @GetMapping("/register/verify")
    public String confirm(@RequestParam String uuid, Model model) {
        String uuidLogin;
        try {
            uuidLogin = registerService.verify(uuid);
        } catch (AttributeNotFoundException e) {
            return "redirect:/error";
        }
        model.addAttribute("uuid", uuidLogin);
        return "verify";
    }
}