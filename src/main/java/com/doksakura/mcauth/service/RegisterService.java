package com.doksakura.mcauth.service;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.exception.StringFormatException;
import com.doksakura.mcauth.model.MailFormat;
import com.doksakura.mcauth.model.RegisterDTO;
import com.doksakura.mcauth.model.RegisterPlayer;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.repository.PlayerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RegisterService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private PlayerRepository repository;

    public String register(RegisterDTO registerDTO) throws StringFormatException, JsonProcessingException {
        if(!registerDTO.getEmail().contains("@")) throw new StringFormatException("รูปแบบอีเมลล์ไม่ถูกต้อง");
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) throw new StringFormatException("รหัสผ่านไม่ตรงกัน");
        if(registerDTO.getPassword().length() < 8) throw new StringFormatException("รหัสผ่านต้องยาวอย่างน้อย 8 ตัว");

        Player player = new Player();
        player.setName(registerDTO.getName());
        player.setEmail(registerDTO.getEmail());
        player.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        RegisterPlayer registerPlayer = new RegisterPlayer();
        registerPlayer.setPlayer(player);
        registerPlayer.setExpired(LocalDateTime.now().plusMinutes(15));
        registerPlayer.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        MapSession.verify.put(player.getName(), registerPlayer);

        String link = "https://sso-mc.doksakura.com/register/verify?uuid=" + registerPlayer.getId();
        MailFormat mailFormat = new MailFormat();
        mailFormat.setTo(player.getEmail());
        mailFormat.setHeader("Verify Email Confirmation - MC Doksakura");
        mailFormat.setBody(kafkaService.mailVerifyString.replace("${email}", player.getEmail().split("@")[0]).replace("${name}", player.getName()).replace("${link}", link));
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaService.produce(objectMapper.writeValueAsString(mailFormat));

        return player.getEmail();
    }

    public String verify(String uuid) throws AttributeNotFoundException {
        RegisterPlayer registerPlayer = null;
        for(RegisterPlayer r : MapSession.verify.values()) {
            if(r.getId().equals(uuid)) {
                registerPlayer = r;
                break;
            }
        }
        if(registerPlayer == null) throw new AttributeNotFoundException();

        Player player = registerPlayer.getPlayer();
        repository.save(player);

        MapSession.verify.remove(player.getName());
        return MapSession.validate.get(player.getName()).getUuid();
    }
}