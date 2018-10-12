package com.middleware.gringott.client.rmi.impl;

import com.middleware.gringott.client.controller.SoketController;
import com.middleware.gringott.client.rmi.services.ServerService;
import com.middleware.gringott.shared.exception.ClientNotFoundException;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.IServer;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getId() throws RemoteException{
        return this.id;
    }

    public void setId(String id) throws RemoteException{
        this.id = id;
    }


    public void submitItem(Item item) throws RemoteException {
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
            log.info("New item registered : {}", item);
            this.items.add(item);
        }

        this.notifySocket();
    }

    @Override
    public void locateServer(String lookup) throws RemoteException {
        this.server.locateServer(lookup);
    }



    @Override
    public void update(Item item) throws RemoteException {
        for (Item i : items) {
            log.info("ids {} {}",i.getId(),item.getId());
            if (i.getId().equals(item.getId()) && !i.isSold()) {
                i.setCurrentPrice(item.getCurrentPrice());
                i.setLeader(item.getLeader());
                i.setSold(item.isSold());
                log.info("Update item : {}", i);
                this.notifySocket();
            }
        }
    }


    private void notifySocket(){
        this.soketController.onReciveItems(
                items.stream()
                        .filter(
                                (i) -> !i.getSeller().equals(pseudo)
                        ).collect(Collectors.toList())
        );

        this.soketController.onReciveMyItems(
                items.stream()
                        .filter(
                                (i) -> i.getSeller().equals(pseudo)
                        ).collect(Collectors.toList())
        );
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
            log.warn("There is a RemoteException problem : {}", e.getMessage());
        } catch (ClientNotFoundException e){
            log.warn("Client already disconected");
        }
    }

}
