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
import java.util.*;


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
        if(clients.containsKey(client.getPseudo())) throw new ClientAlreadyExistExecption("Choose an other pseudo");
        client.setId(UUID.randomUUID().toString());
        this.clients.put(client.getPseudo(), client);
        log.info("The client {} has been registred successfuly", client.getPseudo());
        for (Item i : items) {
            client.addNewItem(i);
        }
    }

    @Override
    public void logout(IClient client) throws RemoteException, ClientNotFoundException {
        synchronized (clients){
            if(!clients.containsKey(client.getPseudo())) throw new ClientNotFoundException("No client with this pseudo");
            log.info("Client {} is logout successfuly", client.getPseudo());
            clients.remove(client.getPseudo());
        }
    }

    @Override
    public void bid(Item item, String buyer) throws RemoteException, ClientNotFoundException {

        if(!clients.containsKey(buyer)) throw new ClientNotFoundException("No client with this pseudo");

        for (Item i : items) {
            if (i.getId().equals(item.getId())){
                if(i.getCurrentPrice() < item.getCurrentPrice()){
                    i.setCurrentPrice(item.getCurrentPrice());
                    i.setLeader(buyer);
                    log.info("New bid from {} recorded for {} at {}", buyer, i.getName(), i.getCurrentPrice());
                    for (IClient c : clients.values()) {
                        c.update(i);
                    }
                }
            }
        }




    }

    @Override
    public void submit(Item item) throws RemoteException, ClientNotFoundException {
        synchronized (items){
            if(!clients.containsKey(item.getSeller())) throw new ClientNotFoundException("No client with this pseudo");
            item.setId(UUID.randomUUID().toString());
            item.setCurrentPrice(item.getPrice());
            log.info("New item registered : {}", item);
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
                                } catch (RemoteException e) {
                                    // Par précotion
                                    try {
                                        log.warn("Client {} unreachable", c.getPseudo());
                                        clients.remove(c.getPseudo());
                                    } catch (RemoteException e1) {
                                        log.warn("There is a RemoteException problem");
                                    }
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
