package com.middleware.gringott.client.controller;

import com.middleware.gringott.shared.impl.SellableItem;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    private IClient client;

    @Autowired
    public HomeController(ApplicationContext context){

        String adress = null;
        try {
            adress = InetAddress.getLocalHost().getHostAddress();
            log.info("RMI host adress : {}", adress);
        } catch (UnknownHostException e) {
            log.info("UnknownHostException {}", e.getMessage());
        }

        System.setProperty("java.rmi.server.hostname", adress);
        this.client = (IClient) context.getBean("rmiClient");
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
            this.client.getServer().submit(sellableItem);
        } catch (RemoteException e) {
            log.warn("RemoteException {}", e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public void login(@RequestBody String name){
        try {
            this.client.setPseudo(name);
            this.client.getServer().registerClient(this.client);
        } catch (RemoteException e) {
            log.warn("RemoteException {}", e.getMessage());
        }
    }

    @GetMapping("/getMesEncheres")
    @ResponseBody
    public List<Item> getMesEnchers(){
        List<Item> lesItems = new ArrayList<>();
        try {
            lesItems =  this.client.getServer().getItems();
        } catch (RemoteException e) {
            log.warn("RemoteException {}", e.getMessage());
        }
        return lesItems;
    }

    @GetMapping("/getEncheres")
    @ResponseBody
    public List<Item> getEncheres(@RequestBody String name){
        List<Item> lesItems = new ArrayList<>();
        try {
            List<Item> items = this.client.getServer().getItems();
            for (int i=0;i < items.size();++i ){
                if (!(items.get(i).getSeller().equals(name))){
                    lesItems.add(items.get(i));
                }
            }
        } catch (RemoteException e) {
            log.info("RemoteException {}", e.getMessage());
        }
        return lesItems;
    }
}
