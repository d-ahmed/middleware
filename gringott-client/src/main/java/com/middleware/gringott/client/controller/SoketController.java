package com.middleware.gringott.client.controller;

import com.middleware.gringott.shared.interfaces.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SoketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SoketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }


    public void onReciveItems(List<Item> items) {
        this.messagingTemplate.convertAndSend("/topic/items", items);
    }


    public void onReciveMyItems(List<Item> items) {
        this.messagingTemplate.convertAndSend("/topic/items/my", items);
    }

    public void onReciveWonItems(List<Item> items) {
        this.messagingTemplate.convertAndSend("/topic/items/won", items);
    }
}
