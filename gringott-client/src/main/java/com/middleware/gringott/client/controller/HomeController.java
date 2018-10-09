package com.middleware.gringott.client.controller;

import com.middleware.gringott.client.rmi.impl.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.rmi.RemoteException;

@Controller
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
}
