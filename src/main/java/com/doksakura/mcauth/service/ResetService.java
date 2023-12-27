package com.doksakura.mcauth.service;

import com.doksakura.mcauth.common.MapSession;
import com.doksakura.mcauth.entity.Player;
import com.doksakura.mcauth.exception.StringFormatException;
import com.doksakura.mcauth.model.MailFormat;
import com.doksakura.mcauth.model.Reset;
import com.doksakura.mcauth.model.ResetFormDTO;
import com.doksakura.mcauth.model.Validate;
import com.doksakura.mcauth.repository.PlayerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetService {

    @Autowired
    private PlayerRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaService kafkaService;

    public String reset(Validate validate) throws AttributeNotFoundException, JsonProcessingException {
        String resetId = UUID.randomUUID().toString().replaceAll("-", "");
        Reset reset = new Reset();
        reset.setResetId(resetId);
        reset.setUuid(validate.getUuid());
        reset.setExpired(validate.getExpired());

        MapSession.resetList.removeIf(reset1 -> reset1.getUuid().equals(validate.getUuid()));
        MapSession.resetList.add(reset);

        Optional<Player> player = repository.findById(validate.getName());
        if(player.isEmpty()) throw new AttributeNotFoundException();

        String link = "https://sso-mc.doksakura.com/confirmreset?resetid=" + resetId;
        MailFormat mailFormat = new MailFormat();
        mailFormat.setTo(player.get().getEmail());
        mailFormat.setHeader("Reset Password Confirmation - MC Doksakura");
        mailFormat.setBody(kafkaService.mailFormatString.replace("${email}", player.get().getEmail().split("@")[0]).replace("${name}", validate.getName()).replace("${link}", link));
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaService.produce(objectMapper.writeValueAsString(mailFormat));

        return player.get().getEmail();
    }

    public void resetForm(ResetFormDTO resetFormDTO) throws AttributeNotFoundException, StringFormatException {
        if(!resetFormDTO.getPassword().equals(resetFormDTO.getConfirmPassword())) throw new StringFormatException("รหัสผ่านไม่ตรงกัน");
        if(resetFormDTO.getPassword().length() < 8) throw new StringFormatException("รหัสผ่านต้องยาวอย่างน้อย 8 ตัว");

        Optional<Reset> reset = MapSession.resetList.stream().filter(reset1 -> reset1.getResetId().equals(resetFormDTO.getResetId())).findFirst();
        if(reset.isEmpty()) throw new AttributeNotFoundException();

        Validate validate = MapSession.getValidate(reset.get().getUuid());
        if(validate == null) throw new AttributeNotFoundException();

        Optional<Player> player = repository.findById(validate.getName());
        if(player.isEmpty()) throw new AttributeNotFoundException();

        player.get().setPassword(passwordEncoder.encode(resetFormDTO.getPassword()));
        repository.save(player.get());

        MapSession.resetList.removeIf(reset1 -> reset1.getResetId().equals(resetFormDTO.getResetId()));
    }
}