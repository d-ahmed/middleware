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
import java.util.stream.Collectors;

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
    public String index() {
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

    @GetMapping("/encheres")
    @ResponseBody
    public List<Item> getEnchers(){
        List<Item> items = new ArrayList<>();
        try {
            String name = this.client.getPseudo();
            items = this.client.getItems()
                    .stream()
                    .filter(
                            (i) -> !i.getSeller().equals(name) &&
                                    !i.isSold()
                    ).collect(Collectors.toList());
        } catch (RemoteException e) {
            log.warn("RemoteException {}", e.getMessage());
        }
        return items;
    }

    @GetMapping("/users/{name}/encheres")
    @ResponseBody
    public List<Item> getMesEnchers(@PathVariable String name){
        List<Item> items = new ArrayList<>();
        try {
            items = this.client.getItems()
                    .stream()
                    .filter(
                            (i) -> i.getSeller().equals(name)
                    ).collect(Collectors.toList());
        } catch (RemoteException e) {
            log.info("RemoteException {}", e.getMessage());
        }
        return items;
    }
}
