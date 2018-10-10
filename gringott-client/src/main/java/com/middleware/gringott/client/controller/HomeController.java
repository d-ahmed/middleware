package com.middleware.gringott.client.controller;

import com.middleware.gringott.shared.exception.ClientNotFoundException;
import com.middleware.gringott.shared.impl.SellableItem;
import com.middleware.gringott.client.rmi.impl.Client;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private Client client;

    public HomeController(){

    }

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws RemoteException {
        model.addAttribute("name", this.client.getPseudo());
        return "index";
    }

    @PostMapping("/sell")
    @ResponseBody
    public void sell(@RequestBody SellableItem sellableItem){
        try {
            sellableItem.setSeller(this.client.getPseudo());
            this.client.submitItem(sellableItem);
        } catch (RemoteException e) {
            log.info("RemoteException {}", e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public void login(@RequestBody String name){
        try {
            this.client.setPseudo(name);
            this.client.getServer().registerClient(this.client);
        } catch (RemoteException e) {
            log.info("RemoteException {}", e.getMessage());
        }
    }

    @GetMapping("/getMesEncheres")
    @ResponseBody
    public List<Item> getMesEnchers(){
        List<Item> lesItems = new ArrayList<>();
        try {
            lesItems =  this.client.getServer().getItems();
        } catch (RemoteException e) {
            log.info("RemoteException {}", e.getMessage());
        }
        return lesItems;
    }
}
