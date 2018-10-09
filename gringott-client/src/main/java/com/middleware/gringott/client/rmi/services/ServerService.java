package com.middleware.gringott.client.rmi.services;

import com.middleware.gringott.shared.interfaces.IServer;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Service
@Getter
public class ServerService {

    private IServer server;

    public void locateServer(String lookup) throws RemoteException {
        try {
            this.server = (IServer) Naming.lookup(lookup);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
