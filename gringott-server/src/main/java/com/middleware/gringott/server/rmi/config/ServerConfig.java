package com.middleware.gringott.server.rmi.config;

import com.middleware.gringott.server.data.entities.SellableItem;
import com.middleware.gringott.server.rmi.impl.Enchere;
import com.middleware.gringott.shared.interfaces.ISoldObservable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;
import com.middleware.gringott.shared.interfaces.IServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ServerConfig {

    @Value("${rmi.host}")
    String host;

    @Bean
    public RmiServiceExporter rmiServiceExporter(ApplicationContext context){

        new Enchere(
            new SellableItem("un oeuf", "une description", 22, "dada", 1),
            new ISoldObservable() {
                @Override
                public void update() {
                    log.info("une mise à jour des clients est requis");
                }
            });

        new Enchere(new SellableItem("un bateau", "une description", 22, "dada", 2),
            new ISoldObservable() {
                @Override
                public void update() {
                    log.info("une mise à jour des clients est requis");
                }
            });



        System.setProperty("java.rmi.server.hostname", this.host);

        RmiServiceExporter exporter = new RmiServiceExporter();

        exporter.setService(context.getBean("rmiEnchereService"));
        exporter.setRegistryPort(1099);
        exporter.setServiceName("enchere");
        exporter.setServiceInterface(IServer.class);

        return exporter;
    }

}
