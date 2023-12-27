package com.doksakura.mcauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private static String TOPIC = "doksakura-mail";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public final String mailFormatString = """
            <meta name="color-scheme" content="light">
                <meta name="supported-color-schemes" content="light">
                <table style="background-color: white; border: none; color: #000000; text-decoration: none;" cellpadding="0" cellspacing="0" width="100%" align="center">
                    <td>
                        <div style="background-color: rgb(56, 87, 60); padding: 1px; text-align: center;">
                            <h3 style="color: rgb(255, 251, 226);">Doksakura Minecraft Server</h3>
                        </div>
                        <div style="background-image: url(https://content.doksakura.com/images/MCBG.jpeg); background-blend-mode: lighten; background-color: rgba(198, 198, 198, 0.536); padding: 20px; background-position-y: -300px; background-position-x: -700px;">
                            Greeting ${email},
                            <br>
                            <br>
                            Reset your password link for player <b>${name}</b>
                            <br><br>
                            ${link}
                            <br><br>
                            Any question please contact to email <b>contact@doksakura.com</b>
                            <br><br>
                            Best regards,
                            <br>
                            Doksakura Team
                        </div>
                    </td>
                </table>""";

    public final String mailVerifyString = """
            <meta name="color-scheme" content="light">
                <meta name="supported-color-schemes" content="light">
                <table style="background-color: white; border: none; color: #000000; text-decoration: none;" cellpadding="0" cellspacing="0" width="100%" align="center">
                    <td>
                        <div style="background-color: rgb(56, 87, 60); padding: 1px; text-align: center;">
                            <h3 style="color: rgb(255, 251, 226);">Doksakura Minecraft Server</h3>
                        </div>
                        <div style="background-image: url(https://content.doksakura.com/images/MCBG.jpeg); background-blend-mode: lighten; background-color: rgba(198, 198, 198, 0.536); padding: 20px; background-position-y: -300px; background-position-x: -700px;">
                            Greeting ${email},
                            <br>
                            <br>
                            Verify your email with this link for player <b>${name}</b>
                            <br><br>
                            ${link}
                            <br><br>
                            Any question please contact to email <b>contact@doksakura.com</b>
                            <br><br>
                            Best regards,
                            <br>
                            Doksakura Team
                        </div>
                    </td>
                </table>""";

    public void produce(String data) {
        this.kafkaTemplate.send(TOPIC, data);
    }
}
