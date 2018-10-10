package com.middleware.gringott.server.rmi.config;

import com.middleware.gringott.shared.impl.SellableItem;
import com.middleware.gringott.server.rmi.impl.Enchere;
import com.middleware.gringott.shared.interfaces.ISoldObservable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;
import com.middleware.gringott.shared.interfaces.IServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@Slf4j
public class ServerConfig {


    @Bean
    public RmiServiceExporter rmiServiceExporter(ApplicationContext context){

        String adress = null;
        try {
            adress = InetAddress.getLocalHost().getHostAddress();
            log.info("RMI host adress : {}", adress);
        } catch (UnknownHostException e) {
            log.info("UnknownHostException {}", e.getMessage());
        }

        System.setProperty("java.rmi.server.hostname", adress);

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setService(context.getBean("rmiEnchereService"));
        exporter.setRegistryPort(1099);
        exporter.setServiceName("enchere");
        exporter.setServiceInterface(IServer.class);

        return exporter;
    }

}
