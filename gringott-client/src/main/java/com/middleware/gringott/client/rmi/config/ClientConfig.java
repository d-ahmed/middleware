package com.middleware.gringott.client.rmi.config;

import com.middleware.gringott.shared.interfaces.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.rmi.RemoteException;

@Configuration
@Slf4j
public class ClientConfig {

    @Value("${rmi.host}")
    String host;



    @Bean
    public IClient createStubAndBind(ApplicationContext context) throws RemoteException {

        System.setProperty("java.rmi.server.hostname", this.host);
        System.out.println(this.host);

        IClient client = (IClient) context.getBean("rmiClient");

        client.locateServer("rmi://localhost:1099/enchere");



        return client;

    }

}
