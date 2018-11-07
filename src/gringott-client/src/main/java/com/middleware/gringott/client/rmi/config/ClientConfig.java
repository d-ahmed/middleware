package com.middleware.gringott.client.rmi.config;

import com.middleware.gringott.shared.interfaces.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

@Configuration
@Slf4j
public class ClientConfig {



    @Value("${rmi.host.server}")
    private String host;

    @Bean
    public IClient createStubAndBind(ApplicationContext context) {



        IClient client = (IClient) context.getBean("rmiClient");

        try {
            client.locateServer("rmi://"+this.host+"/enchere");
        } catch (RemoteException e) {
            log.error("Exception creating connection to {}", this.host);
        }

        return client;

    }

}
