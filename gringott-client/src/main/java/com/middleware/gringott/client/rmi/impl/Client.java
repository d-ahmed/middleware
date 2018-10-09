package com.middleware.gringott.client.rmi.impl;

import com.middleware.gringott.client.rmi.services.ServerService;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.IServer;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@Component("rmiClient")
@Slf4j
public class Client extends UnicastRemoteObject implements IClient {

    @Value("${rmi.host}")
    String host;

    private String pseudo;
    private List<Item> items;

    @Autowired
    private ServerService server;



    public Client() throws RemoteException {
        super();
        this.items = new ArrayList<>();
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
    }

    @Override
    public void endSelling(Item item) throws RemoteException {
        for (Item i : items){
            if (i.getName().equals(item.getName())){
                System.out.println("Fin de la vente : " + i.getName());
                i.setSold(true);
            }
        }
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

}
