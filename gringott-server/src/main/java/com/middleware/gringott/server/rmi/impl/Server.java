package com.middleware.gringott.server.rmi.impl;

import com.middleware.gringott.server.controllers.SoketController;
import com.middleware.gringott.shared.interfaces.ISoldObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.IServer;
import com.middleware.gringott.shared.interfaces.Item;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


@Component("rmiEnchereService")
public class Server implements IServer {

    List<IClient> clients;

    List<Item> items;

    List<Enchere> encheres;

    @Autowired
    SoketController soketController;

    public Server()  {
        this.clients = new ArrayList<>();
        this.items = new ArrayList<>();
        this.encheres = new ArrayList<>();
    }

    @Override
    public void registerClient(IClient client) throws RemoteException {
        System.out.println("New client registered : " + client.getPseudo());
        this.clients.add(client);
        for (Item i : items) {
            client.addNewItem(i);
        }
    }

    // Le log out ne marche pas comme il le faudrait (Le braak dans la boucle)
    @Override
    public void logout(IClient client) throws RemoteException {
        System.out.println(client.getPseudo() + " logged out.");
        for (IClient c : clients) {
            if (c.getPseudo().equals(client.getPseudo())) {
                this.clients.remove(client);
                System.out.println(clients.size() > 0 ? "Still connected : " + clients : "No clients connected now.");
                break;
            }
        }
    }

    @Override
    public void bid(Item item, double newPrice, String buyer) throws RemoteException {

        double price = item.getPrice() + newPrice;
        System.out.println("New bid from " + buyer + " recorded for " + item.getName() + " at " + price);

        Item newItem = item;

        for (Item i : items) {
            if (i.getName().equals(item.getName())){
                i.setPrice(price);
                i.setLeader(buyer);
                newItem = i;
            }
        }


        for (IClient c : clients) {
            c.update(newItem);
        }

        this.soketController.onReciveMessage(items);

    }

    @Override
    public void submit(Item item) throws RemoteException {
        synchronized (items){
            System.out.println("New item registered : " + item);
            this.items.add(item);
            this.encheres.add(new Enchere(
                    item,
                    new ISoldObservable() {
                        @Override
                        public void update() {
                            for (IClient c : clients) {
                                System.out.println(c);
                                try {
                                    c.update(item);
                                } catch (RemoteException e) {
                                    clients.remove(c);
                                }
                            }
                        }
                    }
            ));

            for (IClient c : clients) {
                c.addNewItem(item);
            }

            // this.soketController.onReciveMessage(items);
        }
    }

    @Override
    public List<Item> getItems() {
        return this.items;
    }



    @Override
    public List<IClient> getClients() throws RemoteException {
        return this.clients;
    }
}
