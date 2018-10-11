package com.middleware.gringott.shared.interfaces;

import com.middleware.gringott.shared.exception.ClientAlreadyExistExecption;
import com.middleware.gringott.shared.exception.ClientNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote {

    /**
     * Register a freshly connected client.
     * @param client the new client
     * @throws RemoteException
     */
    void registerClient(IClient client) throws RemoteException, ClientAlreadyExistExecption;

    /**
     * Log out a client.
     * @param client the client to log out.
     * @throws RemoteException
     */
    void logout(IClient client) throws RemoteException, ClientNotFoundException;

    /**
     * Record a new bid from a a client for an item.
     * @param item the item.
     * @param newPrice the bid amount.
     * @param buyer the client.
     * @throws RemoteException
     */
    void bid(Item item, double newPrice, String buyer)  throws RemoteException, ClientNotFoundException;

    /**
     * Record a new Item.
     * @param item the item
     * @throws RemoteException
     */
    void submit(Item item)  throws RemoteException, ClientNotFoundException;

    /**
     * List server's items
     * @return the items.
     * @throws RemoteException
     */
    List<Item> getItems()  throws RemoteException;

    /**
     * List server's clients
     * @return the clients.
     * @throws RemoteException
     */
    List<IClient> getClients() throws RemoteException;


}
