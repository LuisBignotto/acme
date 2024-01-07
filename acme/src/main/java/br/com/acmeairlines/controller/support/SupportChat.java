package br.com.acmeairlines.controller.support;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SupportChat {
    @MessageMapping("/support")
    @SendTo("/ticket")
    public Message sendMessage(Message message){
        return message;
    }

}
