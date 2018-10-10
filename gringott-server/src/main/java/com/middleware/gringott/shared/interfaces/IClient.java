package com.middleware.gringott.shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote {

    /**
     * Add a new sellable item.
     * @param item the item to be sold.
     */
    void addNewItem(Item item) throws RemoteException;

    void locateServer(String lookup) throws RemoteException;

    /**
     * Update an item (after a bid from another buyer).
     * @param item the item to be added.
     * @param newPrice the new price.
     * @param buyer the new leader.
     */
    void update(Item item)  throws RemoteException;


    /**
     * Get client's pseudo.
     */
    String getPseudo() throws RemoteException;

    /**
     * Get client's items.
     */
    List<Item> getItems() throws RemoteException;

    /**
     * Get client's server.
     */
    IServer getServer() throws RemoteException;

    /**
     * Set client's pseudo.
     * @param pseudo the new pseudo
     */
    void setPseudo(String pseudo) throws RemoteException;

}