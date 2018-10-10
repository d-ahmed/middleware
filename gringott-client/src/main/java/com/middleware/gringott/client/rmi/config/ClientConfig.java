package com.middleware.gringott.client.rmi.config;

import com.middleware.gringott.shared.interfaces.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

@Configuration
@Slf4j
public class ClientConfig {



    @Value("${rmi.host.server}")
    private String host;

    @Bean
    public IClient createStubAndBind(ApplicationContext context) throws RemoteException {



        IClient client = (IClient) context.getBean("rmiClient");

        client.locateServer("rmi://"+this.host+"/enchere");

        return client;

    }

}
