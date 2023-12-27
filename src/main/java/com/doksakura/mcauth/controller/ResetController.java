package com.doksakura.mcauth.controller;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.exception.StringFormatException;
import com.doksakura.mcauth.model.ResetDTO;
import com.doksakura.mcauth.model.ResetFormDTO;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.service.LoginService;
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

@Controller
public class ResetController {
    @Autowired
    private LoginService service;
    @Autowired
    private ResetService resetService;

    @GetMapping("/reset")
    public String reset(Model model, @RequestParam String uuid) {
        if(!MapSession.checkInGame(uuid)) return "redirect:/error";

        Player player = service.getPlayer(uuid);
        if(player == null) return "redirect:/error";

        model.addAttribute("name", player.getName());
        model.addAttribute("email", player.getEmail());
        model.addAttribute("uuid", uuid);

        return "reset";
    }

    @PostMapping("/reset")
    public String reset(@ModelAttribute ResetDTO id, Model model) {
        Validate validate = MapSession.getValidate(id.getId());
        if(validate == null) return "redirect:/error";
        String email;
        try {
            email = resetService.reset(validate);
        } catch (AttributeNotFoundException | JsonProcessingException e) {
            return "redirect:/error";
        }

        model.addAttribute("email", email);

        return "resetConfirm";
    }

    @GetMapping("/confirmreset")
    public String confirmReset(Model model, @RequestParam String resetid) {
        if(MapSession.resetList.stream().filter(reset -> reset.getResetId().equals(resetid)).findFirst().isEmpty()) {
            return "redirect:/error";
        }
        model.addAttribute("resetId", resetid);
        return "resetForm";
    }

    @PostMapping("/confirmreset")
    public String confirmReset(Model model, @ModelAttribute ResetFormDTO resetFormDTO) {
        String uuid = MapSession.resetList.stream().filter(reset -> reset.getResetId().equals(resetFormDTO.getResetId())).findFirst().get().getUuid();

        try {
            resetService.resetForm(resetFormDTO);
        } catch (AttributeNotFoundException e) {
            return "redirect:/error";
        } catch (StringFormatException e) {
            model.addAttribute("resetId", resetFormDTO.getResetId());
            model.addAttribute("error", e.getMessage());
            return "resetForm";
        }
        return "redirect:/login?uuid=" + uuid;
    }

}