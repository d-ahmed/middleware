package com.middleware.gringott.server.rmi.impl;

import com.middleware.gringott.shared.exception.ClientAlreadyExistExecption;
import com.middleware.gringott.shared.exception.ClientNotFoundException;
import com.middleware.gringott.shared.interfaces.IObservable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.middleware.gringott.shared.interfaces.IClient;
import com.middleware.gringott.shared.interfaces.IServer;
import com.middleware.gringott.shared.interfaces.Item;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("rmiEnchereService")
@Slf4j
public class Server implements IServer {

    Map<String, IClient> clients;

    List<Item> items;

    List<Observer> observers;

    public Server()  {
        this.clients = new HashMap<>();
        this.items = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerClient(IClient client) throws RemoteException, ClientAlreadyExistExecption {
        System.out.println("New client registered : " + client.getPseudo());
        if(clients.containsKey(client.getPseudo())) throw new ClientAlreadyExistExecption("Choose an other pseudo");
        this.clients.put(client.getPseudo(), client);
        for (Item i : items) {
            client.addNewItem(i);
        }
    }

    @Override
    public void logout(IClient client) throws RemoteException, ClientNotFoundException {
        synchronized (clients){
            if(!clients.containsKey(client.getPseudo())) throw new ClientNotFoundException("No client with this pseudo");
            log.info("Client {} is logout", client.getPseudo());
            clients.remove(client.getPseudo());
        }
    }

    @Override
    public void bid(Item item, double newPrice, String buyer) throws RemoteException, ClientNotFoundException {

        if(!clients.containsKey(buyer)) throw new ClientNotFoundException("No client with this pseudo");

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


        for (IClient c : clients.values()) {
            c.update(newItem);
        }

    }

    @Override
    public void submit(Item item) throws RemoteException, ClientNotFoundException {
        synchronized (items){
            if(!clients.containsKey(item.getSeller())) throw new ClientNotFoundException("No client with this pseudo");
            System.out.println("New item registered : " + item);
            this.items.add(item);
            this.observers.add(new Observer(
                    item.getTime(),
                    new IObservable() {
                        @Override
                        public void update() {
                            for (IClient c : clients.values()) {
                                System.out.println(c);
                                try {
                                    item.setSold(true);
                                    c.update(item);
                                    log.info("Les items {}",items);
                                } catch (RemoteException e) {
                                    clients.remove(c);
                                }
                            }
                        }
                    }
            ));

            for (IClient c : clients.values()) {
                c.addNewItem(item);
            }

        }
    }

    @Override
    public List<Item> getItems() {
        return this.items;
    }



    @Override
    public List<IClient> getClients() throws RemoteException {
        return (List) this.clients.values();
    }
}
