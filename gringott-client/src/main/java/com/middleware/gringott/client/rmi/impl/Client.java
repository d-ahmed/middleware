package com.middleware.gringott.client.rmi.impl;

import com.middleware.gringott.client.controller.SoketController;
import com.middleware.gringott.client.rmi.services.ServerService;
import com.middleware.gringott.shared.exception.ClientNotFoundException;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.IServer;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@Component("rmiClient")
@Slf4j
public class Client extends UnicastRemoteObject implements IClient {

    private String id;

    private String pseudo;
    private List<Item> items;

    @Autowired
    private ServerService server;

    @Autowired
    private SoketController soketController;




    public Client() throws RemoteException {
        super();
        this.items = new ArrayList<>();
    }


    public void submitItem(Item item) throws RemoteException {
        this.items.add(item);
        this.server.getServer().submit(item);
    }


    @Override
    public void addNewItem(Item item) throws RemoteException {
        boolean contains = false;
        for (Item i : items){
            if (i.getName().equals(item.getName())){
                contains = true;
            }
        }
        if (!contains){
            System.out.println("Nouvel item ajouté : " + item.getName());
            this.items.add(item);
        }
        this.soketController.onReciveMessage(items);
    }

    @Override
    public void locateServer(String lookup) throws RemoteException {
        this.server.locateServer(lookup);
    }



    @Override
    public void update(Item item) throws RemoteException {
        for (Item i : items){
            if (i.getName().equals(item.getName()) && !i.isSold()){
                System.out.println("Mise à jour de l'item : " + i.getName());
                i.setPrice(item.getPrice());
                i.setLeader(item.getLeader());
                i.setSold(item.isSold());
            }
        }
        log.info("Item mis à jour {}", item);
        this.soketController.onReciveMessage(items);
    }


    @Override
    public String getPseudo() throws RemoteException {
        return this.pseudo;
    }

    @Override
    public List<Item> getItems() throws RemoteException {
        return this.items;
    }

    @Override
    public IServer getServer() throws RemoteException {
        return this.server.getServer();
    }

    @Override
    public void setPseudo(String pseudo) throws RemoteException {
        this.pseudo = pseudo;
    }

    @PreDestroy
    public void onDestroy() {

        try {
            if(this.getPseudo()!=null) this.server.getServer().logout(this);
        } catch (RemoteException e) {
            log.info("There is a RemoteException problem : {}", e.getMessage());
        } catch (ClientNotFoundException e){
            log.info("Client already disconected");
        }
    }

}
