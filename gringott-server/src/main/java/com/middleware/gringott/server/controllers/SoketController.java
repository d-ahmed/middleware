package com.middleware.gringott.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.middleware.gringott.shared.interfaces.Item;

import java.util.List;

@Controller
public class SoketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SoketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void onReciveMessage(List<Item> items) {
        // Thread.sleep(1000); // simulated delay
        this.messagingTemplate.convertAndSend("/topic/greetings", items);
    }
}
